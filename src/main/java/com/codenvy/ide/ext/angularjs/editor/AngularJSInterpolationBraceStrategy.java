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

            // well we have two {{ then we can close these brackets (and don't forget to add the current character which is {
            if ('{' == beforeChar) {
                command.text = "{}}";
                command.caretOffset = offset + 1;
                command.doit = false;
            }

        } catch (BadLocationException e) {
            Log.error(AngularJSInterpolationBraceStrategy.class, e);
        }


    }
}
