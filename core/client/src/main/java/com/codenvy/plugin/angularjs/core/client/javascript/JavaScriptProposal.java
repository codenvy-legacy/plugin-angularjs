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

import com.codenvy.ide.api.icon.Icon;
import com.codenvy.ide.jseditor.client.codeassist.Completion;
import com.codenvy.ide.jseditor.client.codeassist.CompletionProposal;
import com.codenvy.ide.jseditor.client.document.EmbeddedDocument;
import com.codenvy.ide.jseditor.client.text.LinearRange;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JsProposal;

import elemental.dom.Element;


/**
 * @author Florent Benoit
 */
public class JavaScriptProposal implements CompletionProposal {

    private String prefix;
    private int offset;
    private JsProposal jsProposal;
    private JavaScriptResources javaScriptResources;

    public JavaScriptProposal(String prefix, JsProposal jsProposal, int offset, JavaScriptResources javaScriptResources) {
        super();
        this.prefix = prefix;
        this.jsProposal = jsProposal;
        this.offset = offset;
        this.javaScriptResources = javaScriptResources;
    }

    @Override
    public Element getAdditionalProposalInfo() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return jsProposal.getDescription();
    }

    @Override
    public Icon getIcon() {
        return new Icon("javascript.property", javaScriptResources.property());
    }

    @Override
    public void getCompletion(CompletionCallback completionCallback) {
        completionCallback.onCompletion(new Completion() {
            /** {@inheritDoc} */
            @Override
            public void apply(EmbeddedDocument document) {
                document.replace(offset - prefix.length(), prefix.length(), jsProposal.getProposal());
            }

            /** {@inheritDoc} */
            @Override
            public LinearRange getSelection(EmbeddedDocument document) {
                final int start = offset + jsProposal.getProposal().length() - prefix.length();
                return LinearRange.createWithStart(start).andLength(0);
            }
        });
    }
}
