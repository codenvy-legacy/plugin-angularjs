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
package com.codenvy.plugin.angularjs.template.angularseed;

import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.plugin.angularjs.api.server.AngularProjectTemplateExtension;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the Angular Seed template for AngularJS project type.
 * @author Florent Benoit
 */
@Singleton
public class AngularSeedTemplateExtension implements AngularProjectTemplateExtension {

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<>(1);

        list.add(new ProjectTemplateDescription("Samples - Hello World",
                                                "git",
                                                "AngularJS (Angular seed)",
                                                "Project using Angular seed scaffolding.",
                                                "https://github.com/codenvy-templates/web-angularjs-javascript-angular-seed"));

        return list;
    }
}
