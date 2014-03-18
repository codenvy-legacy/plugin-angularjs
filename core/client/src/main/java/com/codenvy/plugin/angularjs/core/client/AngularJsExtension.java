/*
 * CODENVY CONFIDENTIAL
 *  __________________
 *
 *   [2014] Codenvy, S.A.
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Codenvy S.A. and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Codenvy S.A.
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Codenvy S.A..
 */

package com.codenvy.plugin.angularjs.core.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.util.dom.Elements;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptResources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS extension")

public class AngularJsExtension {

    @Inject
    public AngularJsExtension(IconRegistry iconRegistry) {
        Map<String, String> icons = new HashMap<>();
        icons.put("AngularJS.projecttype.big.icon", "angularjs-extension/newproject-angularjs.png");
        icons.put("AngularJS.projecttype.small.icon", "angularjs-extension/newproject-angularjs.png");

        iconRegistry.registerIcons(icons);
    }
}
