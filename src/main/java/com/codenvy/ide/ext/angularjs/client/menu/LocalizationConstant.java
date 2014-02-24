/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
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
package com.codenvy.ide.ext.angularjs.client.menu;

import com.google.gwt.i18n.client.Messages;

/**
 * Gets properties.
 * @author Florent Benoit
 */
public interface LocalizationConstant extends Messages {

    @Key("control.yeomanDirective.id")
    String yeomanAddDirectiveId();

    @Key("control.yeomanDirective.text")
    String yeomanAddDirectiveText();

    @Key("control.yeomanDirective.description")
    String runAppActionDescription();
}
