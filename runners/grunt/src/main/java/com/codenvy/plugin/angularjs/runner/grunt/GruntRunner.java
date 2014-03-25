/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2014] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.core.util.CustomPortService;
import com.codenvy.api.project.server.ProjectEventService;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.ApplicationProcess;
import com.codenvy.api.runner.internal.DeploymentSources;
import com.codenvy.api.runner.internal.ResourceAllocators;
import com.codenvy.api.runner.internal.Runner;
import com.codenvy.api.runner.internal.RunnerConfiguration;
import com.codenvy.api.runner.internal.RunnerConfigurationFactory;
import com.codenvy.api.runner.internal.dto.RunRequest;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
    public GruntRunner(@Named(DEPLOY_DIRECTORY) java.io.File deployDirectoryRoot,
                       @Named(CLEANUP_DELAY_TIME) int cleanupDelay,
                       @Named("runner.java_webapp.host_name") String hostName,
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
                //FIXME : for now the http port is 9000 by default as it's managed by Grunt
                final int httpPort = portService.acquire();
                final GruntRunnerConfiguration configuration =
                        new GruntRunnerConfiguration(request.getMemorySize(), httpPort, request);
                configuration.getLinks().add(DtoFactory.getInstance().createDto(Link.class).withRel("web url")
                                                       .withHref(String.format("http://%s:%d", hostName, 9000)));
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
            path.mkdirs();
            try {
                ZipUtils.unzip(toDeploy.getFile(), path);

                // then, unzip the grunt addon (if there is no Gruntfile available in the path)
                File gruntFile = new File(path, "Gruntfile.js");
                if (!gruntFile.exists()) {
                    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("builders/grunt-required.zip");
                    ZipUtils.unzip(is, path);
                }

            } catch (IOException e) {
                throw new RunnerException("Unable to unpack the zip file", e);
            }
        } else {

            try (FileReader reader = new FileReader(sourceFile); BufferedReader bufferedReader = new BufferedReader(reader)) {
                path = new File(bufferedReader.readLine());
            } catch (IOException e) {
                throw new RunnerException("Unable to read file", e);
            }
        }

        // Gets the source url from the request
        String sourceURL = configuration.getRequest().getDeploymentSourcesUrl();

        // Create the process
        final GruntProcess process = new GruntProcess(getExecutor(), path, sourceURL);

        //FIXME : cleanup of files ?

        // Add a listener on the project
        RunRequest runRequest = gruntRunnerConfiguration.getRequest();
        String projectName = runRequest.getProjectDescriptor().getName();
        String workspace = runRequest.getWorkspace();

        // Register the listener
        projectEventService.addListener(workspace, projectName, process);

        //FIXME : unregister the listener ?

        return process;
    }

}