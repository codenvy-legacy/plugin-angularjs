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

import com.codenvy.api.project.server.Builders;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.ProjectTypeExtension;
import com.codenvy.api.project.server.Attribute;
import com.codenvy.api.project.server.ProjectTemplateDescription;
import com.codenvy.api.project.server.ProjectType;
import com.codenvy.api.project.server.Runners;
import com.codenvy.ide.Constants;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public class GulpJSProjectTypeExtension implements ProjectTypeExtension {

    @Inject
    private ProjectTypeDescriptionRegistry registry;

    @PostConstruct
    public void init() {
        registry.registerProjectType(this);

    }

    @Override
    public ProjectType getProjectType() {
        return new ProjectType("GruntJS", "GruntJS (javascript)", "JavaScript");
    }

    @Override
    public List<Attribute> getPredefinedAttributes() {
        final List<Attribute> list = new ArrayList<>(1);
        list.add(new Attribute(Constants.LANGUAGE, "javascript"));
        return list;
    }

    @Override
    public Builders getBuilders() {
        return null;
    }

    @Override
    public Runners getRunners() {
        return new Runners("system:/javascript/web/gulp");
    }

    /**
     * Adds all extensions that have been found.
     */
    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> getIconRegistry() {
        return null;
    }

}
