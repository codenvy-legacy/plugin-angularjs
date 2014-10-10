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
package com.codenvy.plugin.angularjs.core.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.icon.IconRegistry;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.projecttype.wizard.ProjectTypeWizardRegistry;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.extension.runner.client.wizard.SelectRunnerPagePresenter;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
import com.codenvy.plugin.angularjs.core.client.wizard.AngularPagePresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "GruntJS")

public class GruntJsExtension extends JsExtension {

    @Inject
    public GruntJsExtension(IconRegistry iconRegistry, AngularJSResources resources, ProjectTypeWizardRegistry projectTypeWizardRegistry,
                            NotificationManager notificationManager, Provider<AngularPagePresenter> angularPagePresenter,
                            Provider<SelectRunnerPagePresenter> runnerPagePresenter) {
        super("GruntJS", iconRegistry, resources, projectTypeWizardRegistry, notificationManager, angularPagePresenter, runnerPagePresenter);

        // add wizard
        ProjectWizard wizard = new ProjectWizard(notificationManager);
        wizard.addPage(angularPagePresenter);
        wizard.addPage(runnerPagePresenter);

        projectTypeWizardRegistry.addWizard("GruntJS", wizard);
    }
}
