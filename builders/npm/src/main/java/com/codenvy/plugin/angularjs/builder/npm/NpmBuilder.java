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
import com.codenvy.dto.server.DtoFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
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

        Runnable runnable = new UpdateCodeRunnable(task.getConfiguration(), dtoFactory);

        Future<?> future = getExecutor().submit(runnable);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new BuilderException("Unable to update source code", e);
        }

        return new BuildResult(true, (File)null);

    }
}