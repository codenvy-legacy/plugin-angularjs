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

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.runner.internal.ApplicationLogger;
import com.google.common.io.CharStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Logger that will print all the logs of the grunt process
 * @author Florent Benoit
 */
public class GruntApplicationLogger implements ApplicationLogger {

    private File logFile;

    public GruntApplicationLogger(File logFile) {
        this.logFile = logFile;
    }

    @Override
    public void getLogs(Appendable output) throws IOException {
        try (Reader r =  new InputStreamReader(new FileInputStream(logFile), "UTF-8")) {
            CharStreams.copy(r, output);
        }
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public void writeLine(String line) throws IOException {
        throw new UnsupportedOperationException("Read-Only logger");
    }

    @Override
    public void close() throws IOException {

    }
}
