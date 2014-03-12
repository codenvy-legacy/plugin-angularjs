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

package com.codenvy.plugin.angularjs.core.client.javascript;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.util.dom.Elements;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "JavaScript extension.", version = "1.0.0")

public class JavaScriptExtension {

    @Inject
    public JavaScriptExtension(JavaScriptResources javaScriptResources) {
        Elements.injectJs(javaScriptResources.esprima().getText() + javaScriptResources.esprimaJsContentAssist().getText());
    }
}
