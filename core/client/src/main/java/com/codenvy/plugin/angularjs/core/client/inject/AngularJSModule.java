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

package com.codenvy.plugin.angularjs.core.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizard;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardProvider;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardSelectNameView;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardSelectNameViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

/**
 * @author Florent Benoit
 */
@ExtensionGinModule
public class AngularJSModule extends AbstractGinModule {

        /** {@inheritDoc} */
        @Override
        protected void configure() {
            bind(DefaultWizard.class).annotatedWith(YeomanWizard.class)
                                     .toProvider(YeomanWizardProvider.class)
                                     .in(Singleton.class);

            bind(YeomanWizardSelectNameView.class).to(YeomanWizardSelectNameViewImpl.class);

        }

}
