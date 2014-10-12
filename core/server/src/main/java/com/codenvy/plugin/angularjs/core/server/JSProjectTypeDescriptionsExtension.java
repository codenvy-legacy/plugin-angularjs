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
package com.codenvy.plugin.angularjs.core.server;

import com.codenvy.api.project.server.ProjectTypeDescriptionExtension;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.AttributeDescription;
import com.codenvy.api.project.server.ProjectType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Register JavaScript extension {@link com.codenvy.api.project.server.ProjectTypeDescriptionExtension} to register project types.
 * @author Florent Benoit
 */
@Singleton
public class JSProjectTypeDescriptionsExtension implements ProjectTypeDescriptionExtension {
    @Inject
    public JSProjectTypeDescriptionsExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Override
    public List<ProjectType> getProjectTypes() {
        final List<ProjectType> list = new ArrayList<>(3);
        list.add(new ProjectType("AngularJS", "AngularJS Project", "JavaScript"));
        list.add(new ProjectType("GruntJS", "Grunt Project", "JavaScript"));
        list.add(new ProjectType("GulpJS", "Gulp Project", "JavaScript"));
        list.add(new ProjectType("BasicJS", "Basic Project", "JavaScript"));
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
