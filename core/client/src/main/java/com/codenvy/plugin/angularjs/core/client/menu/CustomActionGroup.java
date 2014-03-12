/*
 * CODENVY CONFIDENTIAL
 *  __________________
 *
 *   [2014] Codenvy, S.A.
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Codenvy S.A. and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Codenvy S.A.
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Codenvy S.A..
 */

package com.codenvy.plugin.angularjs.core.client.menu;


import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;

/**
 * Allow to hide elements if the current project is not an angular project.
 * @author Florent Benoit
 */
public class CustomActionGroup extends DefaultActionGroup {

    private ResourceProvider resourceProvider;

    public CustomActionGroup(ResourceProvider resourceProvider, String shortName, boolean popup, ActionManager actionManager) {
        super(shortName, popup, actionManager);
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        /*Project activeProject = resourceProvider.getActiveProject();
        if (activeProject != null) {
            final String projectTypeId = activeProject.getDescription().getProjectTypeId();
            boolean isAngularJSProject = "AngularJS".equals(projectTypeId);
            e.getPresentation().setVisible(isAngularJSProject);
            e.getPresentation().setEnabled(isAngularJSProject);
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }*/
    }
}
