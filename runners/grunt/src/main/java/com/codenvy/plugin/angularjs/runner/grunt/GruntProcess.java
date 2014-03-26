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

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.core.util.CommandLine;
import com.codenvy.api.core.util.DownloadPlugin;
import com.codenvy.api.core.util.HttpDownloadPlugin;
import com.codenvy.api.project.server.ProjectEvent;
import com.codenvy.api.project.server.ProjectEventListener;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.ApplicationLogger;
import com.codenvy.api.runner.internal.ApplicationProcess;
import com.codenvy.api.runner.internal.DeploymentSources;
import com.codenvy.commons.lang.ZipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Application process used for Grunt
 *
 * @author Florent Benoit
 */
public class GruntProcess extends ApplicationProcess implements ProjectEventListener {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GruntProcess.class);

    /**
     * Result of the Runtime.exec command
     */
    private Process process;

    /**
     * Excutor service.
     */
    private ExecutorService executorService;

    /**
     * Directory where launch grunt.
     */
    private final File workDir;

    private String         sourceURL;
    private DownloadPlugin downloadPlugin;

    /**
     * Build a new process for the following directory
     *
     * @param workDir
     *         the directory to start grunt
     */
    public GruntProcess(ExecutorService executorService, File workDir, String sourceURL) {
        super();
        this.executorService = executorService;
        this.workDir = workDir;
        this.sourceURL = sourceURL;
        this.downloadPlugin = new HttpDownloadPlugin();
        ;
    }

    /**
     * Run the process is not yet done
     *
     * @throws RunnerException
     *         if command can't be launched
     */
    @Override
    public void start() throws RunnerException {
        if (process != null) {
            throw new IllegalStateException("Process is already started");
        }

        try {
            process = Runtime.getRuntime()
                             .exec(new CommandLine("grunt").add("serve").toShellCommand(), null, workDir);
        } catch (IOException e) {
            throw new RunnerException(e.getCause());
        }
    }

    /**
     * Stop the process
     *
     * @throws RunnerException
     */
    @Override
    public void stop() throws RunnerException {
        if (process == null) {
            throw new IllegalStateException("Process is not started yet");
        }
        process.destroy();
    }


    @Override
    public int waitFor() throws RunnerException {
        synchronized (this) {
            if (process == null) {
                throw new IllegalStateException("Process is not started yet");
            }
        }
        try {
            process.waitFor();
        } catch (InterruptedException ignored) {
        }
        return process.exitValue();
    }

    @Override
    public int exitCode() throws RunnerException {
        if (process == null) {
            return -1;
        }
        return process.exitValue();
    }

    @Override
    public boolean isRunning() throws RunnerException {
        return process != null;
    }

    @Override
    public ApplicationLogger getLogger() throws RunnerException {
        //FIXME : return a real logger
        return ApplicationLogger.DUMMY;
    }


    protected DeploymentSources downloadApplication(String url, File destFolder) throws RunnerException {
        final IOException[] errorHolder = new IOException[1];
        final DeploymentSources[] resultHolder = new DeploymentSources[1];
        final File downloadDir;
        try {
            downloadDir = Files.createTempDirectory(destFolder.toPath(), "updated").toFile();
        } catch (IOException e) {
            throw new RunnerException(e);
        }
        downloadPlugin.download(url, downloadDir, new DownloadPlugin.Callback() {
            @Override
            public void done(java.io.File downloaded) {
                resultHolder[0] = new DeploymentSources(downloaded);
            }

            @Override
            public void error(IOException e) {
                errorHolder[0] = e;
            }
        });
        if (errorHolder[0] != null) {
            throw new RunnerException(errorHolder[0]);
        }
        return resultHolder[0];
    }

    /**
     * There is a change on the project that we're monitoring, whatever the type of event is, we need to updated the runner.
     * @param event
     */
    @Override
    public void onEvent(ProjectEvent event) {
        // needs update
        update();
    }


    /**
     * Update the current code through the executor service
     * Download the new source again and unpack.
     */
    public void update() {
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                DeploymentSources deploymentSources = null;
                try {
                    deploymentSources = downloadApplication(sourceURL, workDir);
                } catch (RunnerException e) {
                    LOG.error("Unable to download project update", e);
                }

                // unpack again
                try {
                    ZipUtils.unzip(deploymentSources.getFile(), workDir);
                } catch (IOException e) {
                    LOG.error("Unable to unzip the update code", e);
                }
                return null;
            }
        });
    }
}
