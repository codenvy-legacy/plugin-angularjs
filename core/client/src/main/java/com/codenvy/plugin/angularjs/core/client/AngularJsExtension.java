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
        iconRegistry.registerIcon("AngularJS.projecttype.big.icon", "angularjs-extension/newproject-angularjs.png");
        iconRegistry.registerIcon("AngularJS.projecttype.small.icon", "angularjs-extension/newproject-angularjs.png");

        // filename icons
        iconRegistry.registerIcon("AngularJS/bower.json.file.small.icon", "angularjs-extension/bower-icon.png");
        iconRegistry.registerIcon("AngularJS/Gruntfile.js.file.small.icon", "angularjs-extension/grunt-icon.png");
        iconRegistry.registerIcon("AngularJS/gulpfile.js.file.small.icon", "angularjs-extension/gulp-icon.png");
        iconRegistry.registerIcon("AngularJS/package.json.file.small.icon", "angularjs-extension/npm-icon.png");
        iconRegistry.registerIcon("AngularJS/README.file.small.icon", "angularjs-extension/text-icon.png");
        iconRegistry.registerIcon("AngularJS/LICENSE.file.small.icon", "angularjs-extension/text-icon.png");

        // register also file extension icons
        iconRegistry.registerIcon("AngularJS/css.file.small.icon", "angularjs-extension/css-icon.png");
        iconRegistry.registerIcon("AngularJS/scss.file.small.icon", "angularjs-extension/css-icon.png");
        iconRegistry.registerIcon("AngularJS/less.file.small.icon", "angularjs-extension/less-icon.png");
        iconRegistry.registerIcon("AngularJS/html.file.small.icon", "angularjs-extension/html-icon.png");
        iconRegistry.registerIcon("AngularJS/js.file.small.icon", "angularjs-extension/js-icon.png");
        iconRegistry.registerIcon("AngularJS/json.file.small.icon", "angularjs-extension/json-icon.png");
        iconRegistry.registerIcon("AngularJS/pom.xml.file.small.icon", "angularjs-extension/maven-icon.png");
        iconRegistry.registerIcon("AngularJS/xml.file.small.icon", "angularjs-extension/xml-icon.png");
        // images
        iconRegistry.registerIcon("AngularJS/gif.file.small.icon", "angularjs-extension/image-icon.png");
        iconRegistry.registerIcon("AngularJS/jpg.file.small.icon", "angularjs-extension/image-icon.png");
        iconRegistry.registerIcon("AngularJS/png.file.small.icon", "angularjs-extension/image-icon.png");

        // text
        iconRegistry.registerIcon("AngularJS/log.file.small.icon", "angularjs-extension/text-icon.png");
        iconRegistry.registerIcon("AngularJS/txt.file.small.icon", "angularjs-extension/text-icon.png");
        iconRegistry.registerIcon("AngularJS/md.file.small.icon", "angularjs-extension/text-icon.png");


    }
}
