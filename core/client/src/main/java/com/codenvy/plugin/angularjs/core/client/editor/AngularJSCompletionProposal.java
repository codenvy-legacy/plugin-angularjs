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

import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.text.RegionImpl;
import com.codenvy.ide.text.edits.MalformedTreeException;
import com.codenvy.ide.text.edits.ReplaceEdit;
import com.codenvy.ide.texteditor.api.codeassistant.Completion;
import com.codenvy.ide.texteditor.api.codeassistant.CompletionProposal;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;


/**
 * AngularJS completion proposal that is called when the user has selected a completion.
 * It will add the property at the right place
 *
 * @author Florent Benoit
 */
public class AngularJSCompletionProposal implements CompletionProposal {

    private static final String PROPERTY_TERMINATOR = "\"\"";
    private static final String PROPERTY_SEPARATOR  = "=";

    private String            name;
    private String            addedString;
    private int               jumpLength;
    private InvocationContext invocationContext;


    public AngularJSCompletionProposal(String name) {
        this.name = name;
    }


    @Override
    public Widget getAdditionalProposalInfo() {
        // if it's an angular directive, return a link to the documentation
        if (!name.startsWith("ng-")) {
            return null;
        }
        // convert the name ng-xxxx into ngXxxx
        String directiveName = "ng".concat(name.substring(3, 4).toUpperCase()).concat(name.substring(4));
        String link = "http://docs.angularjs.org/api/ng/directive/".concat(directiveName);
        // Don't use String.format (not gwt compliant)
        HTML html = new HTML("Full documentation available on <a href='".concat(link).concat("'>").concat(link).concat("</a>"));
        return html;
    }

    @Override
    public String getDisplayString() {
        return new SafeHtmlBuilder().appendEscaped(name).toSafeHtml().asString();
    }

    @Override
    public Image getImage() {
        Image image = new Image();
        image.setResource(invocationContext.getResources().property());
        return image;
    }

    @Override
    public char[] getTriggerCharacters() {
        // when space is entered we may have completion
        return new char[]{' '};
        //return new char[0];
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
                computeInsertedString();
                ReplaceEdit e =
                        new ReplaceEdit(invocationContext.getOffset() - invocationContext.getQuery().getPrefix().length(),
                                        invocationContext.getQuery().getPrefix().length(), addedString);
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
                return new RegionImpl(invocationContext.getOffset() + jumpLength - invocationContext.getQuery().getPrefix().length(), 0);
            }
        });
    }

    /** @return the name */
    public String getName() {
        return name;
    }

    public void setInvocationContext(InvocationContext invocationContext) {
        this.invocationContext = invocationContext;
    }

    protected void computeInsertedString() {
        this.addedString = name + PROPERTY_SEPARATOR + PROPERTY_TERMINATOR;
        // input should be in the middle of quotes
        this.jumpLength = addedString.length() - 1;
    }

}
