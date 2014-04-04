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

package com.codenvy.plugin.angularjs.core.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.util.dom.Elements;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptResources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS extension")

public class AngularJsExtension {

    @Inject
    public AngularJsExtension(IconRegistry iconRegistry) {
        Map<String, String> icons = new HashMap<>();
        iconRegistry.registerIcon("AngularJS.projecttype.big.icon", "angularjs-extension/newproject-angularjs.png");
        iconRegistry.registerIcon("AngularJS.projecttype.small.icon", "angularjs-extension/newproject-angularjs.png");
        iconRegistry.registerIcon("AngularJS/bower.json.file.small.icon", "angularjs-extension/bower-icon.png");
        iconRegistry.registerIcon("AngularJS/Gruntfile.js.file.small.icon", "angularjs-extension/grunt-icon.png");
        iconRegistry.registerIcon("AngularJS/gulpfile.js.file.small.icon", "angularjs-extension/gulp-icon.png");
    }
}
