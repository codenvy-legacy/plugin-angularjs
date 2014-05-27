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

package com.codenvy.plugin.angularjs.core.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

import org.vectomatic.dom.svg.ui.SVGResource;

/**
 * Defines image used for tab completion for AngularJS.
 *
 * @author Florent Benoit
 */
public interface AngularJSResources extends ClientBundle {

    AngularJSResources INSTANCE = GWT.create(AngularJSResources.class);


    @Source("com/codenvy/plugin/angularjs/core/client/completion-item-angularjs.png")
    ImageResource property();

    @Source("com/codenvy/plugin/angularjs/core/client/svg/trash.svg")
    SVGResource trashIcon();

    @Source("com/codenvy/plugin/angularjs/core/client/svg/bower.svg")
    SVGResource bowerFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/css.svg")
    SVGResource cssFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/grunt.svg")
    SVGResource gruntFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/gulp.svg")
    SVGResource gulpFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/html.svg")
    SVGResource htmlFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/image-icon.svg")
    SVGResource imageIcon();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/json.svg")
    SVGResource jsonFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/js.svg")
    SVGResource jsFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/less.svg")
    SVGResource lessFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/maven.svg")
    SVGResource mavenFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/npm.svg")
    SVGResource npmFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/text.svg")
    SVGResource textFile();
    
    @Source("com/codenvy/plugin/angularjs/core/client/svg/xml.svg")
    SVGResource xmlFile();

    @Source({"com/codenvy/plugin/angularjs/core/client/ui.css", "com/codenvy/ide/api/ui/style.css"})
    UiStyle uiCss();

    public interface UiStyle extends CssResource {

        @ClassName("yeoman-wizard-generateButton")
        String yeomanWizardGenerateButton();

        @ClassName("yeoman-wizard-generateButton-icon")
        String yeomanWizardGenerateButtonIcon();

        @ClassName("foldingPanel")
        String foldingPanel();

    }

}
