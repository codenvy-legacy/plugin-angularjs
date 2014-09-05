/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.angularjs.core.client.builder;

import com.codenvy.api.builder.BuildStatus;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.api.builder.dto.BuildTaskDescriptor;
import com.codenvy.api.builder.gwt.client.BuilderServiceClient;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ImportSourceDescriptor;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.BuilderExtension;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.codenvy.ide.extension.builder.client.console.BuilderConsolePresenter;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.util.loging.Log;
import com.codenvy.ide.websocket.Message;
import com.codenvy.ide.websocket.MessageBus;
import com.codenvy.ide.websocket.WebSocketException;
import com.codenvy.ide.websocket.rest.StringUnmarshallerWS;
import com.codenvy.ide.websocket.rest.SubscriptionHandler;
import com.codenvy.ide.websocket.rest.Unmarshallable;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.event.shared.EventBus;

import javax.inject.Inject;
import java.util.List;

import static com.codenvy.ide.api.notification.Notification.Status.FINISHED;
import static com.codenvy.ide.api.notification.Notification.Status.PROGRESS;
import static com.codenvy.ide.api.notification.Notification.Type.ERROR;
import static com.codenvy.ide.api.notification.Notification.Type.INFO;

/**
 * @author Florent Benoit
 */
public class BuilderAgent {

    @Inject
    private DtoFactory dtoFactory;

    @Inject
    private BuilderServiceClient builderServiceClient;

    @Inject
    private AppContext appContext;

    @Inject
    private NotificationManager notificationManager;

    @Inject
    private DtoUnmarshallerFactory dtoUnmarshallerFactory;

    @Inject
    private MessageBus messageBus;

    @Inject
    private EventBus eventBus;

    @Inject
    private BuilderConsolePresenter console;

    @Inject
    private ProjectServiceClient projectServiceClient;


    /**
     * A build has been successful, change notification message.
     * @param notification
     * @param successMessage
     */
    protected void buildSuccessful(Notification notification, String successMessage, String prefixConsole) {
        notification.setMessage(successMessage);
        notification.setStatus(FINISHED);
        console.print(prefixConsole + "::" + successMessage);

    }

    /**
     * Launch a build with all these options
     * @param buildOptions the options to build
     * @param waitMessage the message to display on the notification while waiting
     * @param successMessage the message to display on the notification while it has been successful
     * @param errorMessage the message to display on the notification if there was an error
     * @param prefixConsole the prefix to show in the console
     * @param buildFinishedCallback an optional callback to call when the build has finished
     */
    public void build(final BuildOptions buildOptions, final String waitMessage, final String successMessage, final String errorMessage, final String prefixConsole,
                      final BuildFinishedCallback buildFinishedCallback) {

        // Start a build so print a new notification message
        final Notification notification = new Notification(waitMessage, INFO);
        notificationManager.showNotification(notification);


        // Ask the build service to perform the build
        builderServiceClient.build(appContext.getCurrentProject().getRootProject().getName(),
                                   buildOptions,
                                   new AsyncRequestCallback<BuildTaskDescriptor>(
                                           dtoUnmarshallerFactory.newUnmarshaller(BuildTaskDescriptor.class)) {
                                       @Override
                                       protected void onSuccess(BuildTaskDescriptor result) {
                                           // Notify the callback if already finished
                                           if (result.getStatus() == BuildStatus.SUCCESSFUL) {
                                               if (buildFinishedCallback != null) {
                                                   buildFinishedCallback.onFinished(result.getStatus());
                                               }
                                               buildSuccessful(notification, successMessage, prefixConsole);
                                           } else {
                                               // Check the status by registering a callback
                                               startChecking(notification, result, successMessage, errorMessage, prefixConsole,
                                                             buildFinishedCallback);
                                               notification.setStatus(PROGRESS);
                                           }

                                       }

                                       @Override
                                       protected void onFailure(Throwable exception) {
                                           if (buildFinishedCallback != null) {
                                               buildFinishedCallback.onFinished(BuildStatus.FAILED);
                                           }
                                           notification.setMessage(errorMessage);
                                           notification.setStatus(FINISHED);
                                           notification.setType(ERROR);
                                           notification.setMessage(exception.getMessage());
                                           console.print(prefixConsole + "::" + errorMessage);
                                       }
                                   });


    }

    /**
     * Check the task in order to see if it is being executed and track the result
     * @param notification
     * @param buildTaskDescriptor
     * @param successMessage
     * @param errorMessage
     * @param buildFinishedCallback
     */
    protected void startChecking(final Notification notification, final BuildTaskDescriptor buildTaskDescriptor,
                                     final String successMessage, final String errorMessage, final String prefixConsole,
                                     final BuildFinishedCallback buildFinishedCallback) {

        final SubscriptionHandler<String>  buildOutputHandler = new SubscriptionHandler<String>(new LineUnmarshaller()) {
            @Override
            protected void onMessageReceived(String result) {
                console.print(prefixConsole + "::" + result);
            }

            @Override
            protected void onErrorReceived(Throwable throwable) {
                try {
                    messageBus.unsubscribe(BuilderExtension.BUILD_OUTPUT_CHANNEL + buildTaskDescriptor.getTaskId(), this);
                    Log.error(BuildProjectPresenter.class, throwable);
                } catch (WebSocketException e) {
                    Log.error(BuildProjectPresenter.class, e);
                }
            }
        };

        final SubscriptionHandler<String> buildStatusHandler = new SubscriptionHandler<String>(new StringUnmarshallerWS()) {
            @Override
            protected void onMessageReceived(String result) {
                updateBuildStatus(notification, dtoFactory.createDtoFromJson(result, BuildTaskDescriptor.class), this, buildOutputHandler, successMessage,
                                  errorMessage, prefixConsole, buildFinishedCallback);
            }

            @Override
            protected void onErrorReceived(Throwable exception) {
                try {
                    messageBus.unsubscribe(BuilderExtension.BUILD_STATUS_CHANNEL + buildTaskDescriptor.getTaskId(), this);
                } catch (WebSocketException e) {
                    Log.error(BuildProjectPresenter.class, e);
                }
                notification.setType(ERROR);
                notification.setStatus(FINISHED);
                notification.setMessage(exception.getMessage());
                eventBus.fireEvent(new ExceptionThrownEvent(exception));
                if (buildFinishedCallback != null) {
                    buildFinishedCallback.onFinished(BuildStatus.FAILED);
                }
            }
        };

        try {
            messageBus.subscribe(BuilderExtension.BUILD_STATUS_CHANNEL + buildTaskDescriptor.getTaskId(), buildStatusHandler);
        } catch (WebSocketException e) {
            Log.error(BuildProjectPresenter.class, e);
        }

        try {
            messageBus.subscribe(BuilderExtension.BUILD_OUTPUT_CHANNEL + buildTaskDescriptor.getTaskId(), buildOutputHandler);
        } catch (WebSocketException e) {
            Log.error(BuildProjectPresenter.class, e);
        }

    }



    /**
     * Check for status and display necessary messages.
     *
     * @param descriptor
     *         status of build
     */
    protected void updateBuildStatus(Notification notification, BuildTaskDescriptor descriptor,
                                   SubscriptionHandler<String> buildStatusHandler, SubscriptionHandler<String> buildOutputHandler, final String successMessage, final String errorMessage, final String prefixConsole,
                                   final BuildFinishedCallback buildFinishedCallback) {
        BuildStatus status = descriptor.getStatus();
        if (status == BuildStatus.IN_PROGRESS || status == BuildStatus.IN_QUEUE) {
            return;
        }
        if (status == BuildStatus.CANCELLED || status == BuildStatus.FAILED || status == BuildStatus.SUCCESSFUL) {
            afterBuildFinished(notification, descriptor, buildStatusHandler, buildOutputHandler, successMessage, errorMessage, prefixConsole, buildFinishedCallback);
        }
    }

    /**
     * Perform actions after build is finished.
     *
     * @param descriptor
     *         status of build job
     */
    protected void afterBuildFinished(Notification notification, BuildTaskDescriptor descriptor,
                                    SubscriptionHandler<String> buildStatusHandler, SubscriptionHandler<String> buildOutputHandler, final String successMessage, final String errorMessage, final String prefixConsole,
                                    BuildFinishedCallback buildFinishedCallback) {
        try {
            messageBus.unsubscribe(BuilderExtension.BUILD_STATUS_CHANNEL + descriptor.getTaskId(), buildStatusHandler);
        } catch (Exception e) {
            Log.error(BuildProjectPresenter.class, e);
        }

        try {
            messageBus.unsubscribe(BuilderExtension.BUILD_OUTPUT_CHANNEL + descriptor.getTaskId(), buildOutputHandler);
        } catch (Exception e) {
            Log.error(BuildProjectPresenter.class, e);
        }


        if (descriptor.getStatus() == BuildStatus.SUCCESSFUL) {
            buildSuccessful(notification, successMessage, prefixConsole);
        } else if (descriptor.getStatus() == BuildStatus.FAILED) {
            notification.setMessage(errorMessage);
            notification.setStatus(FINISHED);
            notification.setType(ERROR);
            console.print(prefixConsole + "::" + errorMessage);
        }

        // import zip
        importZipResult(descriptor, buildFinishedCallback);


    }

    /**
     * If there is a result zip, import it.
     * @param descriptor the build descriptor
     * @param buildFinishedCallback the callback to call
     */
    protected void importZipResult(final BuildTaskDescriptor descriptor, final BuildFinishedCallback buildFinishedCallback) {
        Link downloadLink = null;
        List<Link> links = descriptor.getLinks();
        for (Link link : links) {
            if (link.getRel().equalsIgnoreCase("download result")) {
                downloadLink = link;
            }
        }

        if (downloadLink != null) {

            ImportSourceDescriptor importSourceDescriptor =
                    dtoFactory.createDto(ImportSourceDescriptor.class).withLocation(downloadLink.getHref()).withType("zip");

            projectServiceClient.importProject(appContext.getCurrentProject().getRootProject().getPath(), true, importSourceDescriptor, new AsyncRequestCallback<ProjectDescriptor>() {
                @Override
                protected void onSuccess(ProjectDescriptor projectDescriptor) {
                    // notify callback
                    if (buildFinishedCallback != null) {
                        buildFinishedCallback.onFinished(descriptor.getStatus());
                    }
                }

                @Override
                protected void onFailure(Throwable throwable) {
                    if (buildFinishedCallback != null) {
                        buildFinishedCallback.onFinished(descriptor.getStatus());
                    }
                }
            });
        } else {
            // notify callback
            if (buildFinishedCallback != null) {
                buildFinishedCallback.onFinished(descriptor.getStatus());
            }
        }
    }


    static class LineUnmarshaller implements Unmarshallable<String> {
        private String line;

        @Override
        public void unmarshal(Message response) throws UnmarshallerException {
            JSONObject jsonObject = JSONParser.parseStrict(response.getBody()).isObject();
            if (jsonObject == null) {
                return;
            }
            if (jsonObject.containsKey("line")) {
                line = jsonObject.get("line").isString().stringValue();
            }
        }

        @Override
        public String getPayload() {
            return line;
        }
    }
}
