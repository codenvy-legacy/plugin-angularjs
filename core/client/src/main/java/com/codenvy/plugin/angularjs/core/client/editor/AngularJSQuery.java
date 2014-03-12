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

package com.codenvy.plugin.angularjs.core.client.editor;

import java.util.ArrayList;
import java.util.List;

/**
 * Store data about a code completion query.
 *
 * @author Florent Benoit
 */
public class AngularJSQuery {

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;

    private List<String> existingAttributes;

    public AngularJSQuery() {
        this.existingAttributes = new ArrayList<String>();
    }

    public List<String> getExistingAttributes() {
        return existingAttributes;
    }

}
