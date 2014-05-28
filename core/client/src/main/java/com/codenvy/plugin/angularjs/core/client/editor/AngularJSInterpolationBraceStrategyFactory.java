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
