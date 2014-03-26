/**
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

