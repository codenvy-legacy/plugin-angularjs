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
package com.codenvy.plugin.angularjs.core.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.ext.web.html.editor.AutoEditStrategyFactory;
import com.codenvy.ide.ext.web.html.editor.HTMLCodeAssistProcessor;
import com.codenvy.ide.ext.web.js.editor.JsCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSHtmlCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSInterpolationBraceStrategyFactory;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptCodeAssistProcessor;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Singleton;

/**
 * Gin Module for injection of AngularJS plugin.
 * @author Florent Benoit
 */
@ExtensionGinModule
public class AngularJSModule extends AbstractGinModule {

        /** {@inheritDoc} */
        @Override
        protected void configure() {

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
