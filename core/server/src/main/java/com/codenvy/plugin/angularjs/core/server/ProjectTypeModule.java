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

import org.eclipse.che.api.project.server.type.ProjectType;
import org.eclipse.che.inject.DynaModule;
import com.codenvy.plugin.angularjs.core.server.project.type.AngularJSProjectType;
import com.codenvy.plugin.angularjs.core.server.project.type.BasicJSProjectType;
import com.codenvy.plugin.angularjs.core.server.project.type.GruntJSProjectType;
import com.codenvy.plugin.angularjs.core.server.project.type.GulpJSProjectType;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Module for binding type extension.
 *
 * @author Florent Benoit
 */
@DynaModule
public class ProjectTypeModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<ProjectType> projectTypeMultibinder = Multibinder.newSetBinder(binder(), ProjectType.class);
        projectTypeMultibinder.addBinding().to(AngularJSProjectType.class);
        projectTypeMultibinder.addBinding().to(BasicJSProjectType.class);
        projectTypeMultibinder.addBinding().to(GulpJSProjectType.class);
        projectTypeMultibinder.addBinding().to(GruntJSProjectType.class);
    }
}
