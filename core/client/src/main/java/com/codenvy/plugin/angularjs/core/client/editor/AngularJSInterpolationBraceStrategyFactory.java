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


import com.codenvy.ide.ext.web.html.editor.AutoEditStrategyFactory;
import com.codenvy.ide.texteditor.api.AutoEditStrategy;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.google.inject.Singleton;

/**
 * InterpolationBrace Factory for AutoEdit Strategy
 * @author Florent Benoit
 */
@Singleton
public class AngularJSInterpolationBraceStrategyFactory implements AutoEditStrategyFactory {

    /**
     * Build a new Interpolation each time the method is invoked
     * @param textEditorPartView editor view
     * @param contentType content type
     * @return a newly object at each call
     */
    @Override
    public AutoEditStrategy build(TextEditorPartView textEditorPartView, String contentType) {
        return new AngularJSInterpolationBraceStrategy(textEditorPartView);
    }
}
