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

package com.codenvy.ide.ext.angularjs.runner;

import com.codenvy.api.core.util.CommandLine;
import com.codenvy.api.core.util.DownloadPlugin;
import com.codenvy.api.core.util.HttpDownloadPlugin;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.ApplicationLogger;
import com.codenvy.api.runner.internal.ApplicationProcess;
import com.codenvy.api.runner.internal.DeploymentSources;
import com.codenvy.api.runner.internal.Updatable;
import com.codenvy.commons.lang.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Application process used for Grunt
 *
 * @author Florent Benoit
 */
public class GruntProcess extends ApplicationProcess implements Updatable {

    /**
     * Result of the Runtime.exec command
     */
    private Process process;

    /**
     * Directory where launch grunt.
     */
    private final File workDir;

    private String sourceURL;
    private DownloadPlugin downloadPlugin;

    /**
     * Build a new process for the following directory
     *
     * @param workDir
     *         the directory to start grunt
     */
    public GruntProcess(File workDir, String sourceURL) {
        super();
        this.workDir = workDir;
        this.sourceURL = sourceURL;
        this.downloadPlugin = new HttpDownloadPlugin();;
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



    public void update() throws RunnerException {
        // Download the new source again and unpack
        DeploymentSources deploymentSources = downloadApplication(sourceURL, workDir);

        // unpack again
        try {
            ZipUtils.unzip(deploymentSources.getFile(), workDir);
        } catch (IOException e) {
            throw new RunnerException("unable to update");
        }
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
}
