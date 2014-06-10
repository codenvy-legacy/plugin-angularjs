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
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JsProposal;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import org.vectomatic.dom.svg.ui.SVGImage;


/**
 * @author Florent Benoit
 */
public class JavaScriptProposal implements CompletionProposal {

    private String              prefix;
    private int                 offset;
    private JsProposal          jsProposal;
    private JavaScriptResources javaScriptResources;

    public JavaScriptProposal(String prefix, JsProposal jsProposal, int offset, JavaScriptResources javaScriptResources) {
        super();
        this.prefix = prefix;
        this.jsProposal = jsProposal;
        this.offset = offset;
        this.javaScriptResources = javaScriptResources;
    }

    @Override
    public Widget getAdditionalProposalInfo() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return jsProposal.getDescription();
    }

    @Override
    public Image getImage() {
        Image image = new Image();
        image.setResource(javaScriptResources.property());
        return image;
    }


    @Override
    public char[] getTriggerCharacters() {
        return new char[0];
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
                ReplaceEdit e = new ReplaceEdit(offset - prefix.length(), prefix.length(), jsProposal.getProposal());
                try {
                    e.apply(document);
                    // Do not try a new codeassist proposal
                    // invocationContext.getEditor().doOperation(TextEditorOperations.CODEASSIST_PROPOSALS);
                } catch (MalformedTreeException | BadLocationException e1) {
                    Log.error(getClass(), e1);
                }
            }

            /** {@inheritDoc} */
            @Override
            public Region getSelection(Document document) {
                return new RegionImpl(offset + jsProposal.getProposal().length() - prefix.length(), 0);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public SVGImage getSVGImage() {
        // TODO create SVG image to be displayed in JS autocomplition.
        return null;
    }

}
