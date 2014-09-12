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
package com.codenvy.plugin.angularjs.template.yeoman;

import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.plugin.angularjs.api.server.AngularProjectTemplateExtension;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the Yeoman template for AngularJS project type.
 *
 * @author Florent Benoit
 */
@Singleton
public class YeomanTemplateExtension implements AngularProjectTemplateExtension {

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        Map<String, String> params = new HashMap<>(2);
        params.put("branch", "3.1.0");
        params.put("cleanVcs", "true");
        final List<ProjectTemplateDescription> list = new ArrayList<>(1);
        list.add(new ProjectTemplateDescription("Samples - Hello World",
                                                "git",
                                                "AngularJS - Yeoman",
                                                "Project using yeoman scaffolding.",
                                                "https://github.com/codenvy-templates/web-angularjs-javascript-yeoman",
                                                params,
                                                null,
                                                null,
                                                "javascript-webapp-grunt",
                                                null,
                                                null));
        return list;

    }
}
