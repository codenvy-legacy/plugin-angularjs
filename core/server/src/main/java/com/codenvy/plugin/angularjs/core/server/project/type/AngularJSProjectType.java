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
package com.codenvy.plugin.angularjs.core.server.project.type;

import org.eclipse.che.api.project.server.type.ProjectType;
import org.eclipse.che.ide.Constants;
import org.eclipse.che.ide.api.project.type.RunnerCategory;

import javax.inject.Singleton;
import java.util.Arrays;

/**
 * @author Vitaliy Parfonov
 * @author Dmitry Shnurenko
 */
@Singleton
public class AngularJSProjectType extends ProjectType {

    public AngularJSProjectType() {
        super("AngularJS", "AngularJS Project", true, false);
        addConstantDefinition(Constants.LANGUAGE, Constants.LANGUAGE, "javascript");
        addConstantDefinition(Constants.FRAMEWORK, Constants.FRAMEWORK, "AngularJS");
        setDefaultRunner("system:/javascript/webapp/grunt");
        addRunnerCategories(Arrays.asList(RunnerCategory.JAVASCRIPT.toString()));
    }
}
