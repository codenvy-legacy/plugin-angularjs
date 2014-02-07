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
import com.codenvy.api.project.server.VfsPropertyValueProvider;
import com.codenvy.api.project.shared.Attribute;
import com.codenvy.api.project.shared.ProjectType;
import com.codenvy.api.project.shared.ProjectTypeExtension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/** @author Artem Zatsarynnyy */
@Singleton
public class AngularJSProjectTypeExtension implements ProjectTypeExtension {
    @Inject
    public AngularJSProjectTypeExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerProjectType(this);
    }

    @Override
    public ProjectType getProjectType() {
        return new ProjectType("AngularJS", "AngularJS");
    }

    @Override
    public List<Attribute> getPredefinedAttributes() {
        final List<Attribute> list = new ArrayList<>(2);
        list.add(new Attribute("runner.name", new VfsPropertyValueProvider("runner.name", "grunt")));
        list.add(new Attribute("builder.name", new VfsPropertyValueProvider("builder.name", "grunt")));
        return list;
    }
}
