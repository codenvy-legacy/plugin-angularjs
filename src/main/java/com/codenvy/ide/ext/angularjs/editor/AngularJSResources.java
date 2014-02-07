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
package com.codenvy.ide.ext.angularjs.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Defines image used for tab completion for AngularJS.
 *
 * @author Florent Benoit
 */
public interface AngularJSResources extends ClientBundle {

    AngularJSResources INSTANCE = GWT.create(AngularJSResources.class);


    @Source("com/codenvy/ide/ext/angularjs/completion-item-angularjs.png")
    ImageResource property();

    @Source("com/codenvy/ide/ext/angularjs/newproject-angularjs.png")
    ImageResource newAngularJSProject();
}
