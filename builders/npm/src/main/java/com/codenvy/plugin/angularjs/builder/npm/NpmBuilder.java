/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.builder.npm;

import com.codenvy.api.builder.BuilderException;
import com.codenvy.api.builder.internal.BuildResult;
import com.codenvy.api.builder.internal.Builder;
import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.util.CommandLine;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Builder that will use NPM for download dependencies.
 *
 * @author Florent Benoit
 */
@Singleton
public class NpmBuilder extends Builder {

    private DtoFactory dtoFactory;

    /**
     * Default constructor.
     *
     * @param rootDirectory
     *         the directory where we can store data
     * @param numberOfWorkers
     *         the number of workers
     * @param queueSize
     *         the size of the queue
     * @param cleanBuildResultDelay
     *         delay
     */
    @Inject
    public NpmBuilder(@Named(REPOSITORY) File rootDirectory,
                      @Named(NUMBER_OF_WORKERS) int numberOfWorkers,
                      @Named(INTERNAL_QUEUE_SIZE) int queueSize,
                      @Named(CLEAN_RESULT_DELAY_TIME) int cleanBuildResultDelay,
                      EventService eventService) {
        super(rootDirectory, numberOfWorkers, queueSize, cleanBuildResultDelay, eventService);
        this.dtoFactory = DtoFactory.getInstance();
    }


    /**
     * @return the name of this builder
     */
    @Override
    public String getName() {
        return "npm";
    }

    /**
     * @return the description of this builder
     */
    @Override
    public String getDescription() {
        return "NPM package managaer";
    }


    /**
     * Launch NPM to download dependencies
     *
     * @param config
     *         the configuration that may help to build the command line
     * @return the command line
     * @throws BuilderException
     *         if command line can't be build
     */
    @Override
    protected CommandLine createCommandLine(BuilderConfiguration config) throws BuilderException {
        final CommandLine commandLine = new CommandLine("npm");
        // add the given options (like install)
        commandLine.add(config.getTargets());
        return commandLine;
    }

    /**
     * Once NPM has been called, add back files in the projects
     *
     * @param task
     *         task
     * @param successful
     *         reports whether build process terminated normally or not.
     *         Note: {@code true} is not indicated successful build but only normal process termination. Build itself may be unsuccessful
     *         because to compilation error, failed tests, etc.
     * @return
     * @throws BuilderException
     */
    @Override
    protected BuildResult getTaskResult(final FutureBuildTask task, boolean successful) throws BuilderException {
        if (!successful) {
            return new BuildResult(false, (File)null);
        }

        // zip bower folder
        List<File> artifacts = new ArrayList<File>();
        File zipFile = zipNpmFiles(task.getConfiguration());
        artifacts.add(zipFile);

        return new BuildResult(true, artifacts);
    }


    /**
     * Build the zip file of npm modules.
     *
     * @param builderConfiguration
     *         the configuration
     * @return the expected zip file
     * @throws BuilderException
     */
    protected File zipNpmFiles(BuilderConfiguration builderConfiguration) throws BuilderException {
        // get working directory
        File workingDirectory = builderConfiguration.getWorkDir();

        // build zip of node modules containing all the downloaded .js
        File zipFile = new File(workingDirectory, "content.zip");
        File nodeModulesDirectory = new File(workingDirectory, "node_modules");
        try {
            ZipUtils.zipDir(workingDirectory.getPath(), nodeModulesDirectory, zipFile, null);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create archive of the NPM dependencies", e);
        }


        return zipFile;
    }

}