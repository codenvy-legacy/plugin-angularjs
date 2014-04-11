/**
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.runner.gulp;

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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;

/**
 * Runner implementation to run Gulp application
 *
 * @author Florent Benoit
 */
@Singleton
public class GulpRunner extends Runner {

    private       String              hostName;
    private final CustomPortService   portService;
    private final ProjectEventService projectEventService;

    @Inject
    public GulpRunner(@Named(DEPLOY_DIRECTORY) java.io.File deployDirectoryRoot,
                      @Named(CLEANUP_DELAY_TIME) int cleanupDelay,
                      @Named("runner.javascript_gulp.host_name") String hostName,
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
        return "gulp";
    }

    @Override
    public String getDescription() {
        return "gulp.js The streaming build system";
    }

    @Override
    public RunnerConfigurationFactory getRunnerConfigurationFactory() {
        return new RunnerConfigurationFactory() {
            @Override
            public RunnerConfiguration createRunnerConfiguration(RunRequest request) throws RunnerException {
                final GulpRunnerConfiguration configuration =
                        new GulpRunnerConfiguration(request.getMemorySize(), 5000, request);
                configuration.getLinks().add(DtoFactory.getInstance().createDto(Link.class).withRel("web url")
                                                       .withHref(String.format("http://%s:%d", hostName, 5000)));
                return configuration;
            }
        };
    }

    @Override
    protected ApplicationProcess newApplicationProcess(final DeploymentSources toDeploy,
                                                       final RunnerConfiguration configuration) throws RunnerException {
        // Cast the configuration
        if (!(configuration instanceof GulpRunnerConfiguration)) {
            throw new RunnerException("Unable to get the configuration. Not the expected type");
        }

        final GulpRunnerConfiguration gulpRunnerConfiguration = (GulpRunnerConfiguration)configuration;

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

                File gulpFile = new File(path, "gulpfile.js");
                if (!gulpFile.exists()) {
                    throw new RunnerException("Unable to find gulpfile.js file in the project");
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

        // Gets the source url from the request
        String sourceURL = configuration.getRequest().getDeploymentSourcesUrl();

        String baseURL = configuration.getRequest().getProjectDescriptor().getBaseUrl();

        // Create the process
        final GulpProcess process = new GulpProcess(getExecutor(), path, baseURL);

        //FIXME : cleanup of files ?

        // Add a listener on the project
        RunRequest runRequest = gulpRunnerConfiguration.getRequest();
        String projectName = runRequest.getProject();
        String workspace = runRequest.getWorkspace();

        // Register the listener
        projectEventService.addListener(workspace, projectName, process);

        //FIXME : unregister the listener ?

        return process;
    }

}