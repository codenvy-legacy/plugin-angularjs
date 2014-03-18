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
 * Runner implementation to run Java web applications by deploying it to application server.
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class GruntRunner extends Runner {

    private       String            hostName;
    private final CustomPortService portService;

    @Inject
    public GruntRunner(@Named(DEPLOY_DIRECTORY) java.io.File deployDirectoryRoot,
                       @Named(CLEANUP_DELAY_TIME) int cleanupDelay,
                       @Named("runner.java_webapp.host_name") String hostName,
                       ResourceAllocators allocators,
                       CustomPortService portService,
                       EventService eventService) {
        super(deployDirectoryRoot, cleanupDelay, allocators, eventService);
        this.hostName = hostName;
        this.portService = portService;
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

                // Also unzip the grunt addon
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("builders/grunt-required.zip");
                ZipUtils.unzip(is, path);
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


        String sourceURL = configuration.getRequest().getDeploymentSourcesUrl();
        final ApplicationProcess process = new GruntProcess(path, sourceURL);

        //FIXME : cleanup of files ?
        /*registerDisposer(process, new Disposer() {
            @Override
            public void dispose() {
                if (!IoUtil.deleteRecursive(appDir)) {
                    LOG.error("Unable to remove app: {}", appDir);
                }
            }
        });*/

        return process;
    }

}