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

package com.codenvy.plugin.angularjs.core.client.inject;

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.codenvy.ide.ext.web.html.editor.AutoEditStrategyFactory;
import com.codenvy.ide.ext.web.html.editor.HTMLCodeAssistProcessor;
import com.codenvy.ide.ext.web.js.editor.JsCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSHtmlCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSInterpolationBraceStrategyFactory;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.FoldingPanel;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.FoldingPanelFactory;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.FoldingPanelImpl;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.GeneratedItemView;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.GeneratedItemViewFactory;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.GeneratedItemViewImpl;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.YeomanPartView;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.YeomanPartViewImpl;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
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
            // Add the yeoman panel and its sub elements
            bind(YeomanPartView.class).to(YeomanPartViewImpl.class).in(Singleton.class);
            install(new GinFactoryModuleBuilder().implement(FoldingPanel.class, FoldingPanelImpl.class)
                                                 .build(FoldingPanelFactory.class));
            install(new GinFactoryModuleBuilder().implement(GeneratedItemView.class, GeneratedItemViewImpl.class)
                                                 .build(GeneratedItemViewFactory.class));

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
