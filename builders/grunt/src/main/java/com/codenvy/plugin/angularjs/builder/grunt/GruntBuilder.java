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
package com.codenvy.plugin.angularjs.builder.grunt;

import com.codenvy.api.builder.BuilderException;
import com.codenvy.api.builder.internal.BuildResult;
import com.codenvy.api.builder.internal.Builder;
import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.core.util.CommandLine;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                        @Named(CLEAN_RESULT_DELAY_TIME) int cleanBuildResultDelay) {
        super(rootDirectory, numberOfWorkers, queueSize, cleanBuildResultDelay);
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
        try (FileWriter fw = new FileWriter(logFile)) {
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
        try (FileWriter fw = new FileWriter(sourceFile);) {
            fw.write(task.getConfiguration().getWorkDir().getAbsolutePath());
        } catch (IOException e) {
            throw new BuilderException(e);
        }
        List<File> artifacts = new ArrayList<File>();
        artifacts.add(sourceFile);

        return new BuildResult(buildSuccess, artifacts, logFile);
    }

}
