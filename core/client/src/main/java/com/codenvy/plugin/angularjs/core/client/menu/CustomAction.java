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
