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
import com.codenvy.ide.ext.web.html.editor.AutoEditStrategyFactory;
import com.codenvy.ide.ext.web.html.editor.HTMLCodeAssistProcessor;
import com.codenvy.ide.ext.web.html.editor.HTMLEditorConfigurationProvider;
import com.codenvy.ide.ext.web.html.editor.HtmlEditorConfiguration;
import com.codenvy.ide.ext.web.js.editor.JsCodeAssistProcessor;
import com.codenvy.ide.texteditor.api.TextEditorConfiguration;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSHtmlCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSInterpolationBraceStrategyFactory;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizard;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardProvider;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardSelectNameView;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardSelectNameViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.gwt.inject.client.multibindings.GinMultibinder;

/**
 * Gin Module for injection of AngularJS plugin.
 * @author Florent Benoit
 */
@ExtensionGinModule
public class AngularJSModule extends AbstractGinModule {

        /** {@inheritDoc} */
        @Override
        protected void configure() {
            // Wizard for Yeoman
            bind(DefaultWizard.class).annotatedWith(YeomanWizard.class)
                                     .toProvider(YeomanWizardProvider.class)
                                     .in(Singleton.class);
            bind(YeomanWizardSelectNameView.class).to(YeomanWizardSelectNameViewImpl.class);

            // Adds the Auto Edit Strategy (interpolation braces)
            bind(AutoEditStrategyFactory.class).to(AngularJSInterpolationBraceStrategyFactory.class).in(Singleton.class);
            GinMultibinder<AutoEditStrategyFactory> binder = GinMultibinder.newSetBinder(binder(), AutoEditStrategyFactory.class);
            binder.addBinding().to(AngularJSInterpolationBraceStrategyFactory.class);

            // Add HTML completion processors (as being in a set)
            GinMultibinder<HTMLCodeAssistProcessor> binderHtmlProcessors = GinMultibinder.newSetBinder(binder(), HTMLCodeAssistProcessor.class);
            binderHtmlProcessors.addBinding().to(AngularJSHtmlCodeAssistProcessor.class);

            // Add JavaScript completion processors (as being in a set)
            GinMultibinder<JsCodeAssistProcessor> binderJsProcessors = GinMultibinder.newSetBinder(binder(), JsCodeAssistProcessor.class);
            binderJsProcessors.addBinding().to(JavaScriptCodeAssistProcessor.class);

        }

}
