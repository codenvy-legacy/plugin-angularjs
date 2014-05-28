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
import java.util.List;

/**
 * Provides the Yeoman template for AngularJS project type.
 * @author Florent Benoit
 */
@Singleton
public class YeomanTemplateExtension implements AngularProjectTemplateExtension {

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<>(1);
        list.add(new ProjectTemplateDescription("zip",
                                                "AngularJS (Yeoman)",
                                                "Project using yeoman scaffolding.",
                                                "templates/angularjs.zip"));
        return list;

    }
}
