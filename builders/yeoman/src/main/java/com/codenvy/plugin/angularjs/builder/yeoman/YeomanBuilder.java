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

package com.codenvy.plugin.angularjs.builder.yeoman;

import com.codenvy.api.builder.BuilderException;
import com.codenvy.api.builder.internal.BuildListener;
import com.codenvy.api.builder.internal.BuildResult;
import com.codenvy.api.builder.internal.BuildTask;
import com.codenvy.api.builder.internal.Builder;
import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.builder.internal.Constants;
import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.util.CommandLine;
import com.codenvy.commons.lang.ZipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder that will use Yeoman.
 *
 * @author Florent Benoit
 */
@Singleton
public class YeomanBuilder extends Builder implements BuildListener {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(YeomanBuilder.class);

    /**
     * Map between the script to execute and the command line object.
     */
    private Map<CommandLine, File> commandLineToFile;

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
    public YeomanBuilder(@Named(Constants.REPOSITORY) java.io.File rootDirectory,
                         @Named(Constants.NUMBER_OF_WORKERS) int numberOfWorkers,
                         @Named(Constants.INTERNAL_QUEUE_SIZE) int queueSize,
                         @Named(Constants.CLEANUP_RESULT_TIME) int cleanBuildResultDelay,
                         EventService eventService) {
        super(rootDirectory, numberOfWorkers, queueSize, cleanBuildResultDelay, eventService);
        this.commandLineToFile = new HashMap<>();
        getBuildListeners().add(this);
    }


    /**
     * @return the name of this builder
     */
    @Override
    public String getName() {
        return "yeoman";
    }

    /**
     * @return the description of this builder
     */
    @Override
    public String getDescription() {
        return "Yeoman tool";
    }


    /**
     * Creates the yeoman build command line
     *
     * @param config
     *         the configuration that may help to build the command line
     * @return the command line
     * @throws BuilderException
     *         if command line can't be build
     */
    @Override
    protected CommandLine createCommandLine(BuilderConfiguration config) throws BuilderException {

        File workDir = config.getWorkDir();
        File scriptFile = new java.io.File(workDir.getParentFile(), workDir.getName() + ".yo-script");

        // now, write the script
        try (Writer fw = new OutputStreamWriter(new FileOutputStream(scriptFile), "UTF-8")) {
            // for each couple of targets
            List<String> targets = config.getTargets();
            int i = 0;

            // disable anonymous Insight tracking
            while(i < targets.size()) {
                fw.write("yo --no-insight ".concat(targets.get(i++)).concat(" ").concat(targets.get(i++)).concat("\n"));
            }
        } catch (IOException e) {
            throw new BuilderException(e);
        }

        if (!scriptFile.setExecutable(true)) {
            throw new BuilderException("Unable to set executable flag on '" + scriptFile + "'");
        }


        final CommandLine commandLine = new CommandLine(scriptFile.getAbsolutePath());

        // register the command line
        commandLineToFile.put(commandLine, scriptFile);

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
    protected BuildResult getTaskResult(final FutureBuildTask task, boolean successful) throws BuilderException {
        if (!successful) {
            return new BuildResult(false, (File)null);
        }



        // zip bower folder
        List<File> artifacts = new ArrayList<>();
        File zipFile = zipYeomanFiles(task.getConfiguration());
        artifacts.add(zipFile);

        return new BuildResult(true, artifacts);
    }


    /**
     * Build the zip file of yeoman generated stuff
     *
     * @param builderConfiguration
     *         the configuration
     * @return the expected zip file
     * @throws BuilderException
     */
    protected File zipYeomanFiles(BuilderConfiguration builderConfiguration) throws BuilderException {
        // get working directory
        File workingDirectory = builderConfiguration.getWorkDir();

        // build zip containing all the source code
        File zipFile = new File(workingDirectory, "content.zip");
        try {
            ZipUtils.zipDir(workingDirectory.getPath(), workingDirectory, zipFile, null);
        } catch (IOException e) {
            throw new BuilderException("Unable to create archive of the current workspace", e);
        }

        return zipFile;
    }

    @Override
    public void begin(BuildTask task) {

    }

    /**
     * Cleanup the script file once the task has been completed
     * @param task
     */
    @Override
    public void end(BuildTask task) {
        File scriptFile = commandLineToFile.remove(task.getCommandLine());
        if (scriptFile != null) {
            if (!scriptFile.delete()) {
                LOG.warn("Unable to delete ''{0}''", scriptFile);
            }
        }

    }


    public void stop() {
        super.stop();
        // also cleanup scripts
        for (File file : commandLineToFile.values()) {
            if (!file.delete()) {
                LOG.warn("Unable to delete ''{0}''", file);
            }
        }
    }
}