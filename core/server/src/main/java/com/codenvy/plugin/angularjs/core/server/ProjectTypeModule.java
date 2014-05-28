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

import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;

/**
 * Module for Grunt builder.
 *
 * @author Florent Benoit
 */
@DynaModule
public class ProjectTypeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AngularJSProjectTypeDescriptionsExtension.class);
        bind(AngularJSProjectTypeExtension.class);
    }
}
