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
package com.codenvy.plugin.angularjs.core.server;

import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.ProjectTypeExtension;
import com.codenvy.api.project.shared.Attribute;
import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.api.project.shared.ProjectType;
import com.codenvy.ide.ext.java.shared.Constants;
import com.codenvy.plugin.angularjs.api.server.AngularProjectTemplateExtension;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class AngularJSProjectTypeExtension implements ProjectTypeExtension {

    private Map<String, String> icons = new HashMap<>();

    @Inject
    private ProjectTypeDescriptionRegistry registry;

    @com.google.inject.Inject(optional = true)
    private Set<AngularProjectTemplateExtension> extensions;

    @PostConstruct
    public void init() {
        registry.registerProjectType(this);

    }

    @Override
    public ProjectType getProjectType() {
        return new ProjectType("AngularJS", "AngularJS (javascript)");
    }

    @Override
    public List<Attribute> getPredefinedAttributes() {
        final List<Attribute> list = new ArrayList<>(3);
        list.add(new Attribute("language", "javascript"));
        list.add(new Attribute(Constants.LANGUAGE, "javascript"));
        list.add(new Attribute(Constants.FRAMEWORK, "AngularJS"));
        return list;
    }


    /**
     * Adds all extensions that have been found.
     * @return
     */
    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<>();
        if (extensions != null) {
            for (AngularProjectTemplateExtension extension : extensions) {
                list.addAll(extension.getTemplates());
            }
        }
        return list;
    }

}
