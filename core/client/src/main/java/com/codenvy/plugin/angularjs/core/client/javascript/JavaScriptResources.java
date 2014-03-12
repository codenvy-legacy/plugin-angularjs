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

/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.plugin.angularjs.core.client.javascript;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Defines image used for tab completion for AngularJS.
 *
 * @author Florent Benoit
 */
public interface JavaScriptResources extends ClientBundle {

    JavaScriptResources INSTANCE = GWT.create(JavaScriptResources.class);


    @Source("com/codenvy/plugin/angularjs/core/client/completion-item-js.png")
    ImageResource property();

    @Source("com/codenvy/plugin/angularjs/core/client/completion-item-angularjs.png")
    ImageResource propertyAngular();


    @Source("com/codenvy/plugin/angularjs/core/client/esprima/esprima.js")
    TextResource esprima();

    @Source("com/codenvy/plugin/angularjs/core/client/esprima/esprimaJsContentAssist.js")
    TextResource esprimaJsContentAssist();

    @Source("com/codenvy/plugin/angularjs/core/client/templating/angular-completion.json")
    TextResource completionTemplatingJson();


}


