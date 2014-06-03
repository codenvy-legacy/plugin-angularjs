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

import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.core.util.CustomPortService;
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
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;

/**
 * Runner implementation to run Grunt application
 *
 * @author Florent Benoit
 */
@Singleton
public class GruntRunner extends Runner {

    private       String              hostName;
    private final CustomPortService   portService;
    private final ProjectEventService projectEventService;

    @Inject
    public GruntRunner(@Named(Constants.DEPLOY_DIRECTORY) java.io.File deployDirectoryRoot,
                       @Named(Constants.APP_CLEANUP_TIME) int cleanupDelay,
                       @Named("runner.javascript_grunt.host_name") String hostName,
                       ResourceAllocators allocators,
                       CustomPortService portService,
                       EventService eventService,
                       ProjectEventService projectEventService) {
        super(deployDirectoryRoot, cleanupDelay, allocators, eventService);
        this.hostName = hostName;
        this.portService = portService;
        this.projectEventService = projectEventService;

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

            try (Reader reader = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8"); BufferedReader bufferedReader = new BufferedReader(reader)) {
                path = new File(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RunnerException("Unable to read file", e);
            }
        }

        String baseURL = configuration.getRequest().getProjectDescriptor().getBaseUrl();

        // Create the process
        final GruntProcess process = new GruntProcess(getExecutor(), path, baseURL, gruntRunnerConfiguration, this);

        //FIXME : cleanup of files ?

        // Add a listener on the project
        RunRequest runRequest = gruntRunnerConfiguration.getRequest();
        String projectName = runRequest.getProject();
        String workspace = runRequest.getWorkspace();

        // Register the listener
        projectEventService.addListener(workspace, projectName, process);

        return process;
    }


    /**
     * Callback used when the process is being stopped.
     * @param gruntRunnerConfiguration the given configuration
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

}