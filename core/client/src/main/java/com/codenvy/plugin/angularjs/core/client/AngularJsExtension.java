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
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
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
    public AngularJsExtension(IconRegistry iconRegistry, AngularJSResources resources) {
        iconRegistry.registerIcon("AngularJS.projecttype.big.icon", "angularjs-extension/newproject-angularjs.png");
        iconRegistry.registerIcon("AngularJS.projecttype.small.icon", "angularjs-extension/newproject-angularjs.png");

        // filename icons
        iconRegistry.registerSVGIcon("AngularJS/bower.json.file.small.icon", resources.bowerFile());
        iconRegistry.registerSVGIcon("AngularJS/Gruntfile.js.file.small.icon", resources.gruntFile());
        iconRegistry.registerSVGIcon("AngularJS/gulpfile.js.file.small.icon", resources.gulpFile());
        iconRegistry.registerSVGIcon("AngularJS/package.json.file.small.icon", resources.npmFile());
        iconRegistry.registerSVGIcon("AngularJS/README.file.small.icon", resources.textFile());
        iconRegistry.registerSVGIcon("AngularJS/LICENSE.file.small.icon", resources.textFile());

        // register also file extension icons
        iconRegistry.registerSVGIcon("AngularJS/css.file.small.icon", resources.cssFile());
        iconRegistry.registerSVGIcon("AngularJS/scss.file.small.icon", resources.cssFile());
        iconRegistry.registerSVGIcon("AngularJS/less.file.small.icon", resources.lessFile());
        iconRegistry.registerSVGIcon("AngularJS/html.file.small.icon", resources.htmlFile());
        iconRegistry.registerSVGIcon("AngularJS/js.file.small.icon", resources.jsFile());
        iconRegistry.registerSVGIcon("AngularJS/json.file.small.icon", resources.jsonFile());
        iconRegistry.registerSVGIcon("AngularJS/pom.xml.file.small.icon", resources.mavenFile());
        iconRegistry.registerSVGIcon("AngularJS/xml.file.small.icon", resources.xmlFile());
        // images
        iconRegistry.registerSVGIcon("AngularJS/gif.file.small.icon", resources.imageIcon());
        iconRegistry.registerSVGIcon("AngularJS/jpg.file.small.icon", resources.imageIcon());
        iconRegistry.registerSVGIcon("AngularJS/png.file.small.icon", resources.imageIcon());

        // text
        iconRegistry.registerSVGIcon("AngularJS/log.file.small.icon", resources.textFile());
        iconRegistry.registerSVGIcon("AngularJS/txt.file.small.icon", resources.textFile());
        iconRegistry.registerSVGIcon("AngularJS/md.file.small.icon", resources.textFile());


    }
}
