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

package com.codenvy.plugin.angularjs.core.client.menu;


import com.codenvy.api.analytics.logger.AnalyticsEventLogger;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.google.inject.Inject;

/**
 * Allow to hide elements if the current project is not an angular project.
 * @author Florent Benoit
 */
public abstract class CustomAction extends Action {

    private ResourceProvider resourceProvider;

    public CustomAction(ResourceProvider resourceProvider, String name, String description) {
        super(name, description, null);
        this.resourceProvider = resourceProvider;
    }

        /** {@inheritDoc} */
        @Override
        public void update (ActionEvent e){
            Project activeProject = resourceProvider.getActiveProject();
            if (activeProject != null) {
                final String projectTypeId = activeProject.getDescription().getProjectTypeId();
                boolean isAngularJSProject = "AngularJS".equals(projectTypeId);
                e.getPresentation().setVisible(isAngularJSProject);
            } else {
                e.getPresentation().setEnabledAndVisible(false);
            }
        }
    }
