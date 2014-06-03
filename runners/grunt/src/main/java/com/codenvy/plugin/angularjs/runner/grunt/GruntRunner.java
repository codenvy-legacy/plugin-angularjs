/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.builder.BuilderService;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.api.builder.dto.BuildTaskDescriptor;
import com.codenvy.api.core.ConflictException;
import com.codenvy.api.core.ForbiddenException;
import com.codenvy.api.core.NotFoundException;
import com.codenvy.api.core.ServerException;
import com.codenvy.api.core.UnauthorizedException;
import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.rest.HttpJsonHelper;
import com.codenvy.api.core.rest.RemoteServiceDescriptor;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.core.util.CustomPortService;
import com.codenvy.api.core.util.DownloadPlugin;
import com.codenvy.api.core.util.HttpDownloadPlugin;
import com.codenvy.api.core.util.Pair;
import com.codenvy.api.project.server.ProjectEventService;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.dto.RunRequest;
import com.codenvy.api.runner.internal.ApplicationProcess;
import com.codenvy.api.runner.internal.Constants;
import com.codenvy.api.runner.internal.DeploymentSources;
import com.codenvy.api.runner.internal.ResourceAllocators;
import com.codenvy.api.runner.internal.Runner;
import com.codenvy.api.runner.internal.RunnerConfiguration;
import com.codenvy.api.runner.internal.RunnerConfigurationFactory;
import com.codenvy.commons.env.EnvironmentContext;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.commons.user.User;
import com.codenvy.dto.server.DtoFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Runner implementation to run Grunt application
 *
 * @author Florent Benoit
 */
@Singleton
public class GruntRunner extends Runner {

    private final String              hostName;
    private final CustomPortService   portService;
    private final ProjectEventService projectEventService;
    private final String              baseBuilderApiUrl;
    private final DownloadPlugin      downloadPlugin;

    @Inject
    public GruntRunner(@Named(Constants.DEPLOY_DIRECTORY) java.io.File deployDirectoryRoot,
                       @Named(Constants.APP_CLEANUP_TIME) int cleanupDelay,
                       @Nullable @Named("builder.base_api_url") String baseBuilderApiUrl,
                       @Named("runner.javascript_grunt.host_name") String hostName,
                       ResourceAllocators allocators,
                       CustomPortService portService,
                       EventService eventService,
                       ProjectEventService projectEventService) {
        super(deployDirectoryRoot, cleanupDelay, allocators, eventService);
        this.hostName = hostName;
        this.portService = portService;
        this.projectEventService = projectEventService;
        this.baseBuilderApiUrl = baseBuilderApiUrl;
        this.downloadPlugin = new HttpDownloadPlugin();

    }

    @Override
    public String getName() {
        return "grunt";
    }

    @Override
    public String getDescription() {
        return "Grunt JS, the JavaScript Task Runner";
    }

    @Override
    public RunnerConfigurationFactory getRunnerConfigurationFactory() {
        return new RunnerConfigurationFactory() {
            @Override
            public RunnerConfiguration createRunnerConfiguration(RunRequest request) throws RunnerException {

                final int httpPort = portService.acquire();
                final int liveReloadPort = portService.acquire();

                final GruntRunnerConfiguration configuration =
                        new GruntRunnerConfiguration(request.getMemorySize(), httpPort, liveReloadPort, request);
                configuration.getLinks().add(DtoFactory.getInstance().createDto(Link.class).withRel("web url")
                                                       .withHref(String.format("http://%s:%d", hostName, httpPort)));
                return configuration;
            }
        };
    }

    @Override
    protected ApplicationProcess newApplicationProcess(final DeploymentSources toDeploy,
                                                       final RunnerConfiguration configuration) throws RunnerException {
        // Cast the configuration
        if (!(configuration instanceof GruntRunnerConfiguration)) {
            throw new RunnerException("Unable to get the configuration. Not the expected type");
        }

        final GruntRunnerConfiguration gruntRunnerConfiguration = (GruntRunnerConfiguration)configuration;

        // Needs to launch Grunt

        File path;
        File sourceFile = toDeploy.getFile();

        // Zip file, unpack it as it contains the source repository
        if (toDeploy.isArchive()) {
            try {
                path = Files.createTempDirectory(getDeployDirectory().toPath(), null).toFile();
            } catch (IOException e) {
                throw new RunnerException("Unable to create a temporary file", e);
            }
            try {
                ZipUtils.unzip(toDeploy.getFile(), path);

                // then, unzip the grunt addon (if there is no Gruntfile available in the path)
                File gruntFile = new File(path, "Gruntfile.js");
                if (!gruntFile.exists()) {
                    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("runners/grunt-required.zip");
                    ZipUtils.unzip(is, path);
                }

            } catch (IOException e) {
                throw new RunnerException("Unable to unpack the zip file", e);
            }
        } else {

            try (Reader reader = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                path = new File(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RunnerException("Unable to read file", e);
            }
        }

        RunRequest runRequest = gruntRunnerConfiguration.getRequest();
        String projectName = runRequest.getProject();
        String workspace = runRequest.getWorkspace();

        String baseURL = configuration.getRequest().getProjectDescriptor().getBaseUrl();


        // Ask NPM builder for getting NPM dependencies
        File packageJsonFile = new File(path, "package.json");
        if (packageJsonFile.exists()) {
            BuildOptions buildOptions = DtoFactory.getInstance().createDto(BuildOptions.class).withBuilderName("npm").withTargets(
                    Arrays.asList("install"));

            final RemoteServiceDescriptor builderService = getBuilderServiceDescriptor(workspace, baseURL);
            // schedule build
            final BuildTaskDescriptor buildDescriptor;
            try {
                final Link buildLink = builderService.getLink(com.codenvy.api.builder.internal.Constants.LINK_REL_BUILD);
                if (buildLink == null) {
                    throw new RunnerException("Unable get URL for starting build of the application");
                }
                buildDescriptor =
                        HttpJsonHelper.request(BuildTaskDescriptor.class, buildLink, buildOptions, Pair.of("project", projectName));
            } catch (IOException e) {
                throw new RunnerException(e);
            } catch (ServerException | UnauthorizedException | ForbiddenException | NotFoundException | ConflictException e) {
                throw new RunnerException(e.getServiceError());
            }


            final Link buildStatusLink = getLink(com.codenvy.api.builder.internal.Constants.LINK_REL_GET_STATUS,
                                                 buildDescriptor.getLinks());
            if (buildStatusLink == null) {
                throw new RunnerException("Invalid response from builder service. Unable get URL for checking build status");
            }
            String downloadLinkHref;

            // Execute builder
            RemoteBuilderRunnable remoteBuilderRunnable = new RemoteBuilderRunnable(buildDescriptor);

            // Wait the builder
            Future<String> future = getExecutor().submit(remoteBuilderRunnable);
            try {
                downloadLinkHref = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RunnerException("Unable to update source code", e);
            }
            DeploymentSources deploymentSources = downloadApplicationLink(downloadLinkHref, path);
            // unzip it
            if (deploymentSources.getFile().exists()) {
                try (InputStream is = new FileInputStream(deploymentSources.getFile())) {
                    ZipUtils.unzip(is, path);
                } catch (IOException e) {
                    throw new RunnerException("Unable to unzip NPM dependencies");
                }
            }
        }

        // Create the process
        final GruntProcess process = new GruntProcess(getExecutor(), path, baseURL, gruntRunnerConfiguration, this);

        // Register the listener
        projectEventService.addListener(workspace, projectName, process);

        return process;
    }


    private static Link getLink(String rel, List<Link> links) {
        for (Link link : links) {
            if (rel.equals(link.getRel())) {
                return link;
            }
        }
        return null;
    }

    private RemoteServiceDescriptor getBuilderServiceDescriptor(String workspace, String runnerURL) {

        UriBuilder baseBuilderUriBuilder;
        if (baseBuilderApiUrl == null || baseBuilderApiUrl.isEmpty()) {
            baseBuilderUriBuilder = UriBuilder.fromUri(runnerURL.substring(0, runnerURL.indexOf("/project")));
        } else {
            baseBuilderUriBuilder = UriBuilder.fromUri(baseBuilderApiUrl);
        }
        final String builderUrl = baseBuilderUriBuilder.path(BuilderService.class).build(workspace).toString();
        return new RemoteServiceDescriptor(builderUrl);
    }


    /**
     * Callback used when the process is being stopped.
     *
     * @param gruntRunnerConfiguration
     *         the given configuration
     */
    public void onStop(GruntProcess gruntProcess, GruntRunnerConfiguration gruntRunnerConfiguration) {
        if (gruntRunnerConfiguration != null) {
            // free the port numbers
            portService.release(gruntRunnerConfiguration.getHttpPort());
            portService.release(gruntRunnerConfiguration.getLiveReloadPort());


            // remove the listener
            RunRequest runRequest = gruntRunnerConfiguration.getRequest();
            String projectName = runRequest.getProject();
            String workspace = runRequest.getWorkspace();
            projectEventService.removeListener(workspace, projectName, gruntProcess);

        }


    }


    protected DeploymentSources downloadApplicationLink(String url, File destFolder) throws RunnerException {
        DownloadCallback downloadCallback = new DownloadCallback();
        final File downloadDir;

        try {
            downloadDir = Files.createTempDirectory(destFolder.toPath(), "updated").toFile();
        } catch (IOException e) {
            throw new RunnerException(e);
        }
        downloadPlugin.download(url, downloadDir, downloadCallback);
        if (downloadCallback.getErrorHolder() != null) {
            throw new RunnerException(downloadCallback.getErrorHolder());
        }
        return downloadCallback.getResultHolder();
    }


}