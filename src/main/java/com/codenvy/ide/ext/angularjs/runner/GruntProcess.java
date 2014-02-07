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
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.ApplicationLogger;
import com.codenvy.api.runner.internal.ApplicationProcess;

import java.io.File;
import java.io.IOException;

/**
 * Application process used for Grunt
 *
 * @author Florent Benoit
 */
public class GruntProcess extends ApplicationProcess {

    /**
     * Result of the Runtime.exec command
     */
    private Process process;

    /**
     * Directory where launch grunt.
     */
    private final File workDir;

    /**
     * Build a new process for the following directory
     *
     * @param workDir
     *         the directory to start grunt
     */
    public GruntProcess(File workDir) {
        super();
        this.workDir = workDir;
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
}
