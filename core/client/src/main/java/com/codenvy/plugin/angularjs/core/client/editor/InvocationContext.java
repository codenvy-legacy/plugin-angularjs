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

