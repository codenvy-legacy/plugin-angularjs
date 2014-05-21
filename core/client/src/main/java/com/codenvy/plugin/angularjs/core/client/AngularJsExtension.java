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
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.Icon;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.action.Anchor;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.wizard.ProjectTypeWizardRegistry;
import com.codenvy.ide.api.ui.wizard.ProjectWizard;
import com.codenvy.ide.ext.java.shared.Constants;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
import com.codenvy.plugin.angularjs.core.client.wizard.AngularPagePresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_BUILD;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS extension")

public class AngularJsExtension {

    @Inject
    public AngularJsExtension(IconRegistry iconRegistry, AngularJSResources resources, ProjectTypeWizardRegistry projectTypeWizardRegistry, NotificationManager notificationManager, Provider<AngularPagePresenter> angularPagePresenter) {
        iconRegistry.registerIcon(new Icon("AngularJS.projecttype.big.icon", "angularjs-extension/newproject-angularjs.png"));
        iconRegistry.registerIcon(new Icon("AngularJS.projecttype.small.icon", "angularjs-extension/newproject-angularjs.png"));

        // filename icons
        iconRegistry.registerIcon(new Icon("AngularJS/bower.json.file.small.icon", resources.bowerFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/Gruntfile.js.file.small.icon", resources.gruntFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/gulpfile.js.file.small.icon", resources.gulpFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/package.json.file.small.icon", resources.npmFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/README.file.small.icon", resources.textFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/LICENSE.file.small.icon", resources.textFile()));

        // register also file extension icons
        iconRegistry.registerIcon(new Icon("AngularJS/css.file.small.icon", resources.cssFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/scss.file.small.icon", resources.cssFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/less.file.small.icon", resources.lessFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/html.file.small.icon", resources.htmlFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/js.file.small.icon", resources.jsFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/json.file.small.icon", resources.jsonFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/pom.xml.file.small.icon", resources.mavenFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/xml.file.small.icon", resources.xmlFile()));
        // images
        iconRegistry.registerIcon(new Icon("AngularJS/gif.file.small.icon", resources.imageIcon()));
        iconRegistry.registerIcon(new Icon("AngularJS/jpg.file.small.icon", resources.imageIcon()));
        iconRegistry.registerIcon(new Icon("AngularJS/png.file.small.icon", resources.imageIcon()));

        // text
        iconRegistry.registerIcon(new Icon("AngularJS/log.file.small.icon", resources.textFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/txt.file.small.icon", resources.textFile()));
        iconRegistry.registerIcon(new Icon("AngularJS/md.file.small.icon", resources.textFile()));


        // inject CSS
        resources.uiCss().ensureInjected();

        // add wizard
        ProjectWizard wizard = new ProjectWizard(notificationManager);
        wizard.addPage(angularPagePresenter);
        projectTypeWizardRegistry.addWizard("AngularJS", wizard);



    }
}
