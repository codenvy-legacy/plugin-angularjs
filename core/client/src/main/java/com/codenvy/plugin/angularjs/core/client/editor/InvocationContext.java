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

import com.codenvy.ide.texteditor.api.TextEditorPartView;

/**
 * Store data for Angular JS codeassist processor.
 *
 * @author Florent Benoit
 */
public class InvocationContext {
    private final AngularJSQuery query;

    private final int offset;

    private final AngularJSResources resources;

    private final TextEditorPartView editor;

    public InvocationContext(AngularJSQuery query, int offset, AngularJSResources resources, TextEditorPartView editor) {
        super();
        this.query = query;
        this.offset = offset;
        this.resources = resources;
        this.editor = editor;
    }

    public AngularJSQuery getQuery() {
        return query;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @return the resourcess
     */
    public AngularJSResources getResources() {
        return resources;
    }

    /**
     * @return the editor
     */
    public TextEditorPartView getEditor() {
        return editor;
    }
}

