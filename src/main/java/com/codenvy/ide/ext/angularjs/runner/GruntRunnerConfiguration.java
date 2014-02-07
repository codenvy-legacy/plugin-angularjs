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

import com.codenvy.api.runner.internal.RunnerConfiguration;
import com.codenvy.api.runner.internal.dto.RunRequest;

/**
 * Configuration for using Grunt as AngularJS runner.
 *
 * @author Florent Benoit
 */
public class GruntRunnerConfiguration extends RunnerConfiguration {

    private final int httpPort;

    public GruntRunnerConfiguration(int memory, int httpPort, RunRequest runRequest) {
        super(memory, runRequest);
        this.httpPort = httpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    @Override
    public String toString() {
        return "GruntRunnerConfiguration{" +
               "memory=" + getMemory() +
               ", links=" + getLinks() +
               ", request=" + getRequest() +
               ", httpPort='" + getHttpPort() +
               '}';
    }
}
