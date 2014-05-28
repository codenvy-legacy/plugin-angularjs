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

import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.DocumentCommand;
import com.codenvy.ide.text.Position;
import com.codenvy.ide.texteditor.api.AutoEditStrategy;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.util.loging.Log;

/**
 * This strategy will add }} if user is entering {{
 *
 * @author Florent Benoit
 */
public class AngularJSInterpolationBraceStrategy implements AutoEditStrategy {

    private TextEditorPartView textEditorPartView;

    public AngularJSInterpolationBraceStrategy(TextEditorPartView textEditorPartView) {
        this.textEditorPartView = textEditorPartView;
    }

    @Override
    public void customizeDocumentCommand(Document document, DocumentCommand command) {
        // early pruning to slow down normal typing as little as possible
        if (!command.doit || textEditorPartView.getSelection().hasSelection() || command.text.isEmpty()) {
            return;
        }

        // Current character
        final char character = command.text.charAt(0);
        if ('{' != character) {
            return;
        }

        // current cursor position
        Position cursorPosition = textEditorPartView.getSelection().getCursorPosition();
        int offset = cursorPosition.getOffset();

        // not enough characters
        if (offset <= 1) {
            return;
        }


        try {
            // character before the {
            char beforeChar = document.getChar(offset - 1);
            char beforeBeforeChar = document.getChar(offset - 2);

            // well we have two {{ then we can close these brackets (and don't forget to add the current character which is {
            if ('{' == beforeChar && ' ' == beforeBeforeChar) {
                command.text = "{}}";
                command.caretOffset = offset + 1;
                command.doit = false;
            }

        } catch (BadLocationException e) {
            // only debug as we may not have previous characters
            Log.debug(AngularJSInterpolationBraceStrategy.class, e);
        }


    }
}
