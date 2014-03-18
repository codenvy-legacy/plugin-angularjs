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
package com.codenvy.plugin.angularjs.builder.yeoman;

import com.codenvy.api.builder.BuilderException;
import com.codenvy.api.builder.internal.BuildResult;
import com.codenvy.api.builder.internal.Builder;
import com.codenvy.api.builder.internal.BuilderConfiguration;
import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.core.rest.HttpJsonHelper;
import com.codenvy.api.core.util.CommandLine;
import com.codenvy.api.core.util.Pair;
import com.codenvy.api.project.shared.dto.ImportSourceDescriptor;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import org.everrest.core.impl.ContainerResponse;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Builder that will use Yeoman.
 *
 * @author Florent Benoit
 */
@Singleton
public class YeomanBuilder extends Builder {

    private DtoFactory dtoFactory;

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
    public YeomanBuilder(@Named(REPOSITORY) java.io.File rootDirectory,
                        @Named(NUMBER_OF_WORKERS) int numberOfWorkers,
                        @Named(INTERNAL_QUEUE_SIZE) int queueSize,
                        @Named(CLEAN_RESULT_DELAY_TIME) int cleanBuildResultDelay,
                        EventService eventService) {
        super(rootDirectory, numberOfWorkers, queueSize, cleanBuildResultDelay, eventService);
        this.dtoFactory = DtoFactory.getInstance();
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
        final CommandLine commandLine = new CommandLine("yo");
        // add the given options (may be angular:directive <directive>)
        commandLine.add(config.getTargets());
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


        final Runnable runnable = new Runnable() {
            @Override
            public void run()  {
        // get working directory
        File workingDirectory = task.getConfiguration().getWorkDir();
        // perform request to VFS
        try {
            String sourcesURL = task.getConfiguration().getRequest().getSourcesUrl();
            // build import URL ((//FIXME: change the URL to match the new parameter available in the BuildRequest
            String importURL = sourcesURL.replace("/export", "/import");

            // build zip
            File zipFile = new File(workingDirectory, "content.zip");
            ZipUtils.zipDir(workingDirectory.getPath(), workingDirectory, zipFile , null);


            ImportSourceDescriptor importSourceDescriptor = dtoFactory.createDto(ImportSourceDescriptor.class).withLocation(zipFile.getPath()).withType("zip");

            // connect
            HttpURLConnection conn;
            conn = (HttpURLConnection)new URL(importURL).openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("content-type", "application/json");
            //conn.setRequestProperty("Content-type", "application/zip");
            conn.setDoOutput(true);
            //conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Long.toString(zipFile.length()));

           // conn.connect();

            OutputStream output = conn.getOutputStream();
            /*FileInputStream inputStream = new FileInputStream(zipFile);

            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            */

            output.write(dtoFactory.toJson(importSourceDescriptor).getBytes());

            output.close();
            final int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Upload ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            }
        };
        getExecutor().execute(runnable);
        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new BuildResult(true, (File)null);


    }
}