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

package com.codenvy.plugin.angularjs.core.client.javascript;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Defines image used for tab completion for AngularJS.
 *
 * @author Florent Benoit
 */
public interface JavaScriptResources extends ClientBundle {

    JavaScriptResources INSTANCE = GWT.create(JavaScriptResources.class);


    @Source("com/codenvy/plugin/angularjs/core/client/completion-item-js.png")
    ImageResource property();

    @Source("com/codenvy/plugin/angularjs/core/client/completion-item-angularjs.png")
    ImageResource propertyAngular();


    @Source("com/codenvy/plugin/angularjs/core/client/esprima/esprima.js")
    TextResource esprima();

    @Source("com/codenvy/plugin/angularjs/core/client/esprima/esprimaJsContentAssist.js")
    TextResource esprimaJsContentAssist();

    @Source("com/codenvy/plugin/angularjs/core/client/templating/angular-completion.json")
    TextResource completionTemplatingJson();


}


