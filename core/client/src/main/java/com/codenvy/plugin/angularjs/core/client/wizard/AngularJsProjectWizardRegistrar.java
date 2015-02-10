/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.angularjs.core.client.wizard;

import com.codenvy.api.project.shared.dto.ImportProject;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizardRegistrar;
import com.codenvy.ide.api.wizard.WizardPage;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;

/**
 * Provides information for registering AngularJS project type into project wizard.
 *
 * @author Artem Zatsarynnyy
 */
public class AngularJsProjectWizardRegistrar implements ProjectWizardRegistrar {
    private final Array<Provider<? extends WizardPage<ImportProject>>> wizardPages;

    @Inject
    public AngularJsProjectWizardRegistrar() {
        wizardPages = Collections.createArray();
    }

    @Nonnull
    public String getProjectTypeId() {
        return "AngularJS";
    }

    @Nonnull
    public String getCategory() {
        return "JavaScript";
    }

    @Nonnull
    public Array<Provider<? extends WizardPage<ImportProject>>> getWizardPages() {
        return wizardPages;
    }
}
