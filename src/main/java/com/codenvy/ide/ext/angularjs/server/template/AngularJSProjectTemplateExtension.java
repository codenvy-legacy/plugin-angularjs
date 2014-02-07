/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2014] Codenvy, S.A.
 *  All Rights Reserved.
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
package com.codenvy.ide.ext.angularjs.server.template;

import com.codenvy.api.project.server.ProjectTemplateRegistry;
import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.api.project.shared.ProjectTemplateExtension;
import com.codenvy.api.project.shared.ProjectType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Template displaying AngularJS template. Registering yeoman project.
 * @author Florent Benoit
 */
@Singleton
public class AngularJSProjectTemplateExtension implements ProjectTemplateExtension {

    @Inject
    public AngularJSProjectTemplateExtension(ProjectTemplateRegistry registry) {
        registry.register(this);
    }

    @Override
    public ProjectType getProjectType() {
        return new ProjectType("AngularJS", "AngularJS (javascript)");
    }

    @Override
    public List<ProjectTemplateDescription> getTemplateDescriptions() {
        final List<ProjectTemplateDescription> list = new ArrayList<>(1);
        list.add(new ProjectTemplateDescription("angularjs_yeoman",
                                                "AngularJS PROJECT",
                                                "Project using yeoman scaffolding.",
                                                "templates/angularjs.zip"));
        return list;
    }
}
