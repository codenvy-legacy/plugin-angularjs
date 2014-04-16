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

package com.codenvy.plugin.angularjs.runner.gulp;

import com.codenvy.api.runner.internal.RunnerConfiguration;
import com.codenvy.api.runner.dto.RunRequest;

/**
 * Configuration for using Gulp as AngularJS runner.
 *
 * @author Florent Benoit
 */
public class GulpRunnerConfiguration extends RunnerConfiguration {

    private final int httpPort;

    public GulpRunnerConfiguration(int memory, int httpPort, RunRequest runRequest) {
        super(memory, runRequest);
        this.httpPort = httpPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    @Override
    public String toString() {
        return "GulpRunnerConfiguration{" +
               "memory=" + getMemory() +
               ", links=" + getLinks() +
               ", request=" + getRequest() +
               ", httpPort='" + getHttpPort() +
               '}';
    }
}
