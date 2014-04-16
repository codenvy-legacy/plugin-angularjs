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

package com.codenvy.plugin.angularjs.core.client.builder;

import com.codenvy.api.builder.BuildStatus;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.api.builder.dto.BuildTaskDescriptor;
import com.codenvy.api.builder.gwt.client.BuilderServiceClient;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.BuilderExtension;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.codenvy.ide.websocket.MessageBus;
import com.codenvy.ide.websocket.WebSocketException;
import com.codenvy.ide.websocket.rest.StringUnmarshallerWS;
import com.codenvy.ide.websocket.rest.SubscriptionHandler;
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
    private ResourceProvider resourceProvider;

    @Inject
    private NotificationManager notificationManager;

    @Inject
    private DtoUnmarshallerFactory dtoUnmarshallerFactory;

    @Inject
    private MessageBus messageBus;

    @Inject
    private EventBus eventBus;

    @Inject
    private ConsolePart console;


    /**
     * A build has been successful, change notification message.
     * @param notification
     * @param successMessage
     */
    protected void buildSuccessful(Notification notification, String successMessage) {
        notification.setMessage(successMessage);
        notification.setStatus(FINISHED);

    }

    /**
     * Launch a build with all these options
     * @param buildOptions the options to build
     * @param waitMessage the message to display on the notification while waiting
     * @param successMessage the message to display on the notification while it has been successful
     * @param errorMessage the message to display on the notification if there was an error
     * @param buildFinishedCallback an optional callback to call when the build has finished
     */
    public void build(final BuildOptions buildOptions, final String waitMessage, final String successMessage, final String errorMessage,
                      final BuildFinishedCallback buildFinishedCallback) {

        // Start a build so print a new notification message
        final Notification notification = new Notification(waitMessage, INFO);
        notificationManager.showNotification(notification);


        // Ask the build service to perform the build
        builderServiceClient.build(resourceProvider.getActiveProject().getPath(),
                                   buildOptions,
                                   new AsyncRequestCallback<BuildTaskDescriptor>(
                                           dtoUnmarshallerFactory.newUnmarshaller(BuildTaskDescriptor.class)) {
                                       @Override
                                       protected void onSuccess(BuildTaskDescriptor result) {
                                           // Notify the callback if already finised
                                           if (result.getStatus() == BuildStatus.SUCCESSFUL) {
                                               if (buildFinishedCallback != null) {
                                                   buildFinishedCallback.onFinished(result.getStatus());
                                               }
                                               buildSuccessful(notification, successMessage);
                                           } else {
                                               // Check the status by registering a callback
                                               startCheckingStatus(notification, result, successMessage, errorMessage,
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
    protected void startCheckingStatus(final Notification notification, final BuildTaskDescriptor buildTaskDescriptor,
                                     final String successMessage, final String errorMessage,
                                     final BuildFinishedCallback buildFinishedCallback) {
        final SubscriptionHandler<String> buildStatusHandler = new SubscriptionHandler<String>(new StringUnmarshallerWS()) {
            @Override
            protected void onMessageReceived(String result) {
                updateBuildStatus(notification, dtoFactory.createDtoFromJson(result, BuildTaskDescriptor.class), this, successMessage,
                                  errorMessage, buildFinishedCallback);
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
    }


    /**
     * Check for status and display necessary messages.
     *
     * @param descriptor
     *         status of build
     */
    protected void updateBuildStatus(Notification notification, BuildTaskDescriptor descriptor,
                                   SubscriptionHandler<String> buildStatusHandler, final String successMessage, final String errorMessage,
                                   final BuildFinishedCallback buildFinishedCallback) {
        BuildStatus status = descriptor.getStatus();
        if (status == BuildStatus.IN_PROGRESS || status == BuildStatus.IN_QUEUE) {
            return;
        }
        if (status == BuildStatus.CANCELLED || status == BuildStatus.FAILED || status == BuildStatus.SUCCESSFUL) {
            afterBuildFinished(notification, descriptor, buildStatusHandler, successMessage, errorMessage, buildFinishedCallback);
        }
    }

    /**
     * Perform actions after build is finished.
     *
     * @param descriptor
     *         status of build job
     */
    protected void afterBuildFinished(Notification notification, BuildTaskDescriptor descriptor,
                                    SubscriptionHandler<String> buildStatusHandler, final String successMessage, final String errorMessage,
                                    BuildFinishedCallback buildFinishedCallback) {
        try {
            messageBus.unsubscribe(BuilderExtension.BUILD_STATUS_CHANNEL + descriptor.getTaskId(), buildStatusHandler);
        } catch (Exception e) {
            Log.error(BuildProjectPresenter.class, e);
        }

        if (descriptor.getStatus() == BuildStatus.SUCCESSFUL) {
            buildSuccessful(notification, successMessage);
        } else if (descriptor.getStatus() == BuildStatus.FAILED) {
            notification.setMessage(errorMessage);
            notification.setStatus(FINISHED);
            notification.setType(ERROR);
        }
        getBuildLogs(descriptor);
        // notify callback
        if (buildFinishedCallback != null) {
            buildFinishedCallback.onFinished(descriptor.getStatus());
        }
    }

    protected void getBuildLogs(BuildTaskDescriptor descriptor) {
        Link statusLink = null;
        List<Link> links = descriptor.getLinks();
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            if (link.getRel().equalsIgnoreCase("view build log"))
                statusLink = link;
        }

        builderServiceClient.log(statusLink, new AsyncRequestCallback<String>(new StringUnmarshaller()) {
            @Override
            protected void onSuccess(String result) {
                console.print(result);
            }

            @Override
            protected void onFailure(Throwable exception) {
                String msg = "Fail to get result";
                console.print(msg);
                Notification notification = new Notification(msg, ERROR);
                notificationManager.showNotification(notification);
            }
        });
    }
}
