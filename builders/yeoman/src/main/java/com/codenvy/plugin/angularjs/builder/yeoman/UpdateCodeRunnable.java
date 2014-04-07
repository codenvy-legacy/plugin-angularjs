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

package com.codenvy.plugin.angularjs.builder.yeoman;

import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.project.shared.dto.ImportSourceDescriptor;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        // build zip containing all the source code
        File zipFile = new File(workingDirectory, "content.zip");
        try {
            ZipUtils.zipDir(workingDirectory.getPath(), workingDirectory, zipFile, null);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create archive of the current workspace", e);
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
