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

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.util.dom.Elements;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "JavaScript extension.", version = "1.0.0")

public class JavaScriptExtension {

    @Inject
    public JavaScriptExtension(JavaScriptResources javaScriptResources) {
        Elements.injectJs(javaScriptResources.esprima().getText() + javaScriptResources.esprimaJsContentAssist().getText());
    }
}
