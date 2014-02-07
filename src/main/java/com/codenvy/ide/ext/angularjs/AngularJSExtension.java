/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */

package com.codenvy.ide.ext.angularjs;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.template.TemplateAgent;
import com.codenvy.ide.api.ui.wizard.template.AbstractTemplatePage;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.angularjs.editor.AngularJSResources;
import com.codenvy.ide.resources.ProjectTypeAgent;
import com.codenvy.ide.resources.model.Property;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import static com.codenvy.ide.collections.Collections.createArray;

/**
 * AngularJS extension for IDE3
 *
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS", version = "3.0.0", description = "AngularJS support")
public class AngularJSExtension {

    public static final String ANGULARJS_PROJECT_TYPE   = "AngularJS";
    public static final String ANGULARJS_PROJECT_NATURE = "Java";
    public static final String ANGULARJS_TEMPLATE       = "AngularJS";


    /**
     * Extension providing HTML/JS angular JS completion.
     */
    @Inject
    public AngularJSExtension(ProjectTypeAgent projectTypeAgent, TemplateAgent templateAgent) {
/*
        Array<Property> properties = Collections.createArray();

        properties.add(new Property("nature.mixin", Collections.createArray("AngularJS project")));
        properties.add(new Property("runner.name", Collections.createArray("grunt")));
        properties.add(new Property("vfs:projectType", Collections.createArray("AngularJS project")));
        properties.add(new Property("nature.primary", Collections.createArray("javascript")));
        properties.add(new Property("vfs:mimeType", Collections.createArray("text/vnd.ideproject+directory")));
        properties.add(new Property("builder.name", Collections.createArray("grunt")));

/*
        projectTypeAgent.register(ANGULARJS_PROJECT_TYPE,
                                  "AngularJS application",
                                  AngularJSResources.INSTANCE.newAngularJSProject(),
                                  ANGULARJS_PROJECT_NATURE,
                                  createArray(ANGULARJS_PROJECT_NATURE),
                                  properties);

        templateAgent.register(ANGULARJS_TEMPLATE,
                               ANGULARJS_TEMPLATE,
                               null,
                               ANGULARJS_PROJECT_NATURE,
                               createArray(ANGULARJS_PROJECT_NATURE),
                               Collections.<Provider<? extends AbstractTemplatePage>>createArray(angularJSTemplateProvider));
*/

    }
}
