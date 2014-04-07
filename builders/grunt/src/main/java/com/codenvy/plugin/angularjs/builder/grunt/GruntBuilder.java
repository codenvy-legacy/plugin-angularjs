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

package com.codenvy.plugin.angularjs.builder.grunt;

import com.codenvy.api.builder.BuilderException;
import com.codenvy.api.builder.internal.BuildResult;
import com.codenvy.api.builder.internal.Builder;
import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.util.CommandLine;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder that will run grunt build.
 *
 * @author Florent Benoit
 */
@Singleton
public class GruntBuilder extends Builder {

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
    public GruntBuilder(@Named(REPOSITORY) java.io.File rootDirectory,
                        @Named(NUMBER_OF_WORKERS) int numberOfWorkers,
                        @Named(INTERNAL_QUEUE_SIZE) int queueSize,
                        @Named(CLEAN_RESULT_DELAY_TIME) int cleanBuildResultDelay,
                        EventService eventService) {
        super(rootDirectory, numberOfWorkers, queueSize, cleanBuildResultDelay, eventService);
    }

    /**
     * @return the name of this builder
     */
    @Override
    public String getName() {
        return "grunt";
    }

    /**
     * @return the description of this builder
     */
    @Override
    public String getDescription() {
        return "Grunt JS, the JavaScript Task Runner";
    }

    /**
     * Creates the grunt build command line
     *
     * @param config
     *         the configuration that may help to build the command line
     * @return the command line
     * @throws BuilderException
     *         if command line can't be build
     */
    @Override
    protected CommandLine createCommandLine(BuilderConfiguration config) throws BuilderException {
        final CommandLine commandLine = new CommandLine("grunt");
        switch (config.getTaskType()) {
            case DEFAULT:
                commandLine.add("build");
                break;
            default:
        }
        commandLine.add(config.getOptions());
        return commandLine;
    }

    /**
     * Build a dummy artifact containing the path to the builder as the runner may require this artifact.
     * Also the result will contain the log of this command line
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
    protected BuildResult getTaskResult(FutureBuildTask task, boolean successful) throws BuilderException {
        if (!successful) {
            return new BuildResult(false, (File)null);
        }

        //FIXME : log in another place ?

        File logFile = new File(task.getConfiguration().getWorkDir(), "log-builder.txt");
        File sourceFile = new File(task.getConfiguration().getWorkDir(), ".datapath");

        // Check if the build has been aborted
        boolean buildSuccess = true;
        BufferedReader logReader = null;
        try (Writer fw = new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8")) {
            logReader = new BufferedReader(task.getBuildLogger().getReader());
            String line;
            while ((line = logReader.readLine()) != null) {
                if (line.contains("Aborted due to warnings")) {
                    buildSuccess = false;
                }
                // Dump the log result
                fw.write(line);
            }
        } catch (IOException e) {
            throw new BuilderException(e);
        } finally {
            if (logReader != null) {
                try {
                    logReader.close();
                } catch (IOException ignored) {
                }
            }
        }


        // FIXME : we may want to zip the current source folder ?
        // for now add the path to the directory
        try (Writer fw = new OutputStreamWriter(new FileOutputStream(sourceFile), "UTF-8")) {
            fw.write(task.getConfiguration().getWorkDir().getAbsolutePath());
        } catch (IOException e) {
            throw new BuilderException(e);
        }
        List<File> artifacts = new ArrayList<File>();
        artifacts.add(sourceFile);

        return new BuildResult(buildSuccess, artifacts, logFile);
    }

}
