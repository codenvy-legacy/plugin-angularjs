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

import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.shared.AttributeDescription;
import com.codenvy.api.project.shared.ProjectType;
import com.codenvy.api.project.server.ProjectTypeDescriptionExtension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Register angularjs extension {@link com.codenvy.api.project.server.ProjectTypeDescriptionExtension} to register project types.
 * @author Florent Benoit
 */
@Singleton
public class AngularJSProjectTypeDescriptionsExtension implements ProjectTypeDescriptionExtension {
    @Inject
    public AngularJSProjectTypeDescriptionsExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Override
    public List<ProjectType> getProjectTypes() {
        final List<ProjectType> list = new ArrayList<>(1);
        list.add(new ProjectType("AngularJS", "AngularJS project"));
        return list;
    }

    @Override
    public List<AttributeDescription> getAttributeDescriptions() {
        final List<AttributeDescription> list = new ArrayList<>(2);
        list.add(new AttributeDescription("builder.name"));
        list.add(new AttributeDescription("runner.name"));
        return list;
    }
}
