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
package com.codenvy.plugin.angularjs.core.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.icon.IconRegistry;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "GulpJS")

public class GulpJsExtension extends JsExtension {

    @Inject
    public GulpJsExtension(IconRegistry iconRegistry, AngularJSResources resources, NotificationManager notificationManager) {
        super("GulpJS", iconRegistry, resources, notificationManager);
    }
}
