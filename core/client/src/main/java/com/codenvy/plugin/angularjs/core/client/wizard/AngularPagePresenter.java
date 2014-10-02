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
package com.codenvy.plugin.angularjs.core.client.wizard;

import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.api.wizard.AbstractWizardPage;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.Nullable;

/**
 * @author Florent Benoit
 */
@Singleton
public class AngularPagePresenter extends AbstractWizardPage implements AngularPageView.ActionDelegate {

    private AngularPageView view;

    @Inject
    public AngularPagePresenter(AngularPageView view) {
        super("AngularJS project settings", null);
        this.view = view;
        view.setDelegate(this);
    }

    @Nullable
    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void focusComponent() {

    }

    /**
     * This page is only there to setup project and display nothing so we should skip this page
     */
    @Override
    public boolean canSkip() {
        return true;
    }

    @Override
    public void removeOptions() {

    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
        ProjectDescriptor project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                }
            });
        }
    }

}
