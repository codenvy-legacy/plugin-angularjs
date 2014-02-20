package com.codenvy.ide.ext.angularjs.menu;

import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.resources.model.Project;

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
