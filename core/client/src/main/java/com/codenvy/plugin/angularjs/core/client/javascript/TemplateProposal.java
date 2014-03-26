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

package com.codenvy.plugin.angularjs.core.client.javascript;

import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.text.RegionImpl;
import com.codenvy.ide.text.edits.MalformedTreeException;
import com.codenvy.ide.text.edits.ReplaceEdit;
import com.codenvy.ide.texteditor.api.codeassistant.Completion;
import com.codenvy.ide.texteditor.api.codeassistant.CompletionProposal;
import com.codenvy.ide.util.loging.Log;
import com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptResources;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 */
public class TemplateProposal implements CompletionProposal {

    private String              prefix;
    private int                 offset;
    private String              replaceName;
    private String              displayName;
    private JavaScriptResources javaScriptResources;
    private boolean isMethod = false;

    public TemplateProposal(String prefix, String displayName, String replaceName, int offset, JavaScriptResources javaScriptResources) {
        super();
        this.prefix = prefix;
        this.displayName = displayName;
        this.replaceName = replaceName;
        this.offset = offset;
        this.javaScriptResources = javaScriptResources;
    }

/*
    public Point getSelection(IDocument document) {
        int escapePosition = prop.getEscapePosition();
        if (escapePosition == -1) {
            escapePosition = offset + prop.getProposal().length();
        }
        return new Point(escapePosition, 0);
    }*/

    @Override
    public Widget getAdditionalProposalInfo() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return displayName;
    }

    @Override
    public Image getImage() {
        Image image = new Image();
        image.setResource(javaScriptResources.propertyAngular());
        return image;
    }


    @Override
    public char[] getTriggerCharacters() {
        return null;
    }

    @Override
    public boolean isAutoInsertable() {
        return true;
    }

    @Override
    public void getCompletion(CompletionCallback completionCallback) {
        completionCallback.onCompletion(new Completion() {
            /** {@inheritDoc} */
            @Override
            public void apply(Document document) {
                ReplaceEdit e = new ReplaceEdit(offset - prefix.length(), prefix.length(), replaceName);
                try {
                    e.apply(document);
                    // Do not try a new codeassist proposal
                    // invocationContext.getEditor().doOperation(TextEditorOperations.CODEASSIST_PROPOSALS);
                } catch (MalformedTreeException e1) {
                    Log.error(getClass(), e1);
                } catch (BadLocationException e1) {
                    Log.error(getClass(), e1);
                }
            }

            /** {@inheritDoc} */
            @Override
            public Region getSelection(Document document) {
                if (isMethod) {
                    // search parenthesis
                    int leftPar = replaceName.substring(prefix.length()).indexOf("(");
                    int rightPar = replaceName.substring(replaceName.indexOf("(")).indexOf(")");
                    return new RegionImpl(offset + leftPar + 1, rightPar - 1);
                }
                return new RegionImpl(offset + replaceName.length() - prefix.length(), 0);
            }
        });
    }


    public void setMethod() {
        this.isMethod = true;
    }

}
