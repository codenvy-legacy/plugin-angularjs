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

package com.codenvy.plugin.angularjs.builder.bower;

import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.project.shared.dto.ImportSourceDescriptor;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;
import com.codenvy.dto.server.JsonArrayImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Update VFS source code with generated code.
 *
 * @author Florent Benoit
 */
public class UpdateCodeRunnable implements Runnable {

    /**
     * Configuration used to extract parameters.
     */
    private BuilderConfiguration configuration;

    /**
     * DTO factory used for generating JSON requests.
     */
    private DtoFactory dtoFactory;

    private static final Pattern DIRECTORY_PATTERN = Pattern.compile("\"directory\"\\s*:\\s*\"(.*)\"");


    /**
     * Build a new instance.
     * @param configuration
     * @param dtoFactory
     */
    public UpdateCodeRunnable(BuilderConfiguration configuration, DtoFactory dtoFactory) {
        this.configuration = configuration;
        this.dtoFactory = dtoFactory;
    }

    /**
     * Perform the update
     */
    @Override
    public void run() {
        // get working directory
        File workingDirectory = configuration.getWorkDir();
        // perform request to VFS
        String sourcesURL = configuration.getRequest().getSourcesUrl();
        // build import URL ((//FIXME: change the URL to match the new parameter available in the BuildRequest
        String importURL = sourcesURL.replace("/export", "/import");

        // Default bower directory
        File appDirectory = new File(workingDirectory, "app");
        File bowerComponentsDirectory = new File(appDirectory, "bower_components");


        // Check bower.rc resource ?
        File bowerRcResource = new File(workingDirectory, ".bowerrc");
        if (bowerRcResource.exists()) {
            // read content
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = Files.newBufferedReader(bowerRcResource.toPath(), Charset.defaultCharset());) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to read the .bowerrc file", e);
            }

            String jsonContent = stringBuilder.toString();
            // parse
            Matcher paramMatcher = DIRECTORY_PATTERN.matcher(jsonContent);

            if (paramMatcher.find()) {
                String directoryName = paramMatcher.group(1);
                bowerComponentsDirectory = new File(workingDirectory, directoryName);
            }
        }

        // build zip of node modules containing all the downloaded .js
        File zipFile = new File(workingDirectory, "content.zip");

        if (bowerComponentsDirectory.exists()) {
            try {
                ZipUtils.zipDir(workingDirectory.getPath(), bowerComponentsDirectory, zipFile, null);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to create archive of the NPM dependencies", e);
            }
        } else {
            throw new IllegalStateException("No app/bower_components directory found");
        }

        ImportSourceDescriptor importSourceDescriptor =
                dtoFactory.createDto(ImportSourceDescriptor.class).withLocation(zipFile.getPath()).withType("zip");

        // connect
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(importURL).openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create HTTP request", e);
        }

        try (OutputStream output = conn.getOutputStream()) {
            output.write(dtoFactory.toJson(importSourceDescriptor).getBytes("UTF-8"));

            final int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException(String.format("Unable to send the updating zip. Status code is %s", responseCode));
            }
        } catch (IOException e){
            throw new IllegalStateException("Unable to create HTTP request", e);
        }
    }

}
