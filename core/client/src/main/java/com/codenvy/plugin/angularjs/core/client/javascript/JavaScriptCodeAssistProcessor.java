/*
 * CODENVY CONFIDENTIAL
 *  __________________
 *
 *   [2014] Codenvy, S.A.
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Codenvy S.A. and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Codenvy S.A.
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Codenvy S.A..
 */

package com.codenvy.plugin.angularjs.core.client.javascript;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.plugin.angularjs.completion.dto.Method;
import com.codenvy.plugin.angularjs.completion.dto.Param;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;
import com.codenvy.plugin.angularjs.completion.dto.Templating;
import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.texteditor.api.codeassistant.CompletionProposal;
import com.codenvy.ide.util.AbstractTrie;
import com.codenvy.ide.util.loging.Log;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.Context;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JavaScriptContentAssistProvider;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JsProgram;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JsProposal;
import com.google.gwt.core.client.JsArray;

import java.util.List;

/**
 * @author Florent Benoit
 */
public class JavaScriptCodeAssistProcessor implements CodeAssistProcessor {

    private static char[] activationCharacters = new char[]{'.'};

    private boolean isTextToCompleteBeforeDot;

    private native JavaScriptContentAssistProvider getProvider()/*-{
        return $wnd.jsEsprimaContentAssistProvider;
    }-*/;

    private JavaScriptContentAssistProvider provider;

    private JavaScriptResources javaScriptResources;

    private Templating templating;

    private AbstractTrie<TemplateDotProvider> trie;

    public JavaScriptCodeAssistProcessor(Templating templating, JavaScriptResources javaScriptResources) {
        this.templating = templating;
        this.javaScriptResources = javaScriptResources;
        provider = getProvider();

        // build trie
        buildTrie();
    }

    /** Complete will all stuff except directives for now
     * */
    protected void buildTrie() {
        this.trie = new AbstractTrie<TemplateDotProvider>();
        for (TemplateDotProvider provider : templating.getTemplateDotProviders()) {
            if ("DIRECTIVE".equals(provider.getType())) {
                continue;
            }
            trie.put(provider.getName(), provider);
        }
    }

    @Override
    public void computeCompletionProposals(TextEditorPartView view, int offset, CodeAssistCallback codeAssistCallback) {
        Context c = Context.create();
        String prefix = computePrefix(view.getDocument(), offset);
        c.setPrefix(prefix);
        Array<CompletionProposal> prop = Collections.createArray();


        String templatePrefix = computeTemplatePrefix(view.getDocument(), offset);

        int dot = 0;
        int lastDot = Integer.MAX_VALUE;


        if (templatePrefix.length() > 0) {
            dot = templatePrefix.indexOf('.');
            lastDot = templatePrefix.lastIndexOf('.');
        }


        if (dot != -1 && dot == lastDot) {
            // get the current template provider
            String prefixVal = templatePrefix.substring(0, dot);
            String suffixVal = templatePrefix.substring(dot + 1, templatePrefix.length());
            AbstractTrie<String> subTrie = new AbstractTrie<String>();

            for (TemplateDotProvider provider : templating.getTemplateDotProviders()) {
                if (prefixVal.equals(provider.getName())) {
                    // add all methods
                    List<Method> methods = provider.getMethods();
                    if (methods != null) {
                        for (Method m : methods) {
                            String fullName = m.getName();
                            if (m.getParams() != null && m.getParams().size() > 0) {
                                fullName = fullName.concat("(");
                                int i = 1;
                                for (Param param : m.getParams()) {
                                        fullName = fullName.concat(param.getName());
                                        if (i < m.getParams().size()) {
                                            fullName = fullName.concat(",");
                                        }
                                        i++;
                                    }
                                    fullName = fullName.concat(")");
                                } else {
                                    fullName = fullName.concat("()");
                                }
                                subTrie.put(m.getName(), fullName);
                            }
                        }
                    }
                }
                Array<String> result = subTrie.search(suffixVal);
                for (String st : result.asIterable()) {
                    TemplateProposal templateProposal = new TemplateProposal(templatePrefix, st, prefixVal.concat(".").concat(st), offset, javaScriptResources);
                    templateProposal.setMethod();
                    prop.add(templateProposal);
                }
            } else if (dot == -1) {
             // Perform completion only if there is no dot
            Array<TemplateDotProvider> result = trie.search(prefix);
            for (TemplateDotProvider provider : result.asIterable()) {
                prop.add(new TemplateProposal(templatePrefix, provider.getName(), provider.getName(), offset, javaScriptResources));
            }


        }




        JsProgram jsProgram = provider.parse(view.getDocument().get());

        try {
            JsArray<JsProposal> jsProposals = provider.computeProposals(view.getDocument().get(), offset, c);
            if (jsProposals != null && jsProposals.length() != 0) {
                for (int i = 0; i < jsProposals.length(); i++) {
                    prop.add(new JavaScriptProposal(prefix, jsProposals.get(i), offset, javaScriptResources));
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        // TODO
        /*if (!isTextToCompleteBeforeDot) {
            Array<? extends TemplateProposal> search = JsConstants.getInstance().getTemplatesTrie().search(prefix);
            for (TemplateProposal p : search.asIterable()) {
                p.setOffset(offset);
                p.setPrefix(prefix);
                prop.add(p);
            }
        }*/
        CompletionProposal[] proposals = new CompletionProposal[prop.size()];
        for (int i = 0; i < prop.size(); i++) {
            proposals[i] = prop.get(i);
        }

        codeAssistCallback.proposalComputed(proposals);

    }

    /**
     * @param document
     * @param offset
     * @return
     */
    private String computeTemplatePrefix(Document document, int offset) {
        try {
            Region lineInfo = document.getLineInformationOfOffset(offset);
            String line = document.get(lineInfo.getOffset(), lineInfo.getLength());
            String partLine = line.substring(0, offset - lineInfo.getOffset());
            return partLine;
        } catch (BadLocationException e) {
            Log.error(JavaScriptCodeAssistProcessor.class, e);
        }
        return "";
    }


    /**
     * @param document
     * @param offset
     * @return
     */
    private String computePrefix(Document document, int offset) {
        isTextToCompleteBeforeDot = false;
        try {
            Region lineInfo = document.getLineInformationOfOffset(offset);
            String line = document.get(lineInfo.getOffset(), lineInfo.getLength());
            String partLine = line.substring(0, offset - lineInfo.getOffset());
            for (int i = partLine.length() - 1; i >= 0; i--) {
                switch (partLine.charAt(i)) {
                    case '.':
                        isTextToCompleteBeforeDot = true;
                    case ' ':
                    case '(':
                    case ')':
                    case '{':
                    case '}':
                    case ';':
                    case '[':
                    case ']':
                    case '"':
                    case '\'':
                        return partLine.substring(i + 1);
                    default:
                        break;
                }
            }
            return partLine;

        } catch (BadLocationException e) {
            Log.error(JavaScriptCodeAssistProcessor.class, e);
        }
        return "";
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return activationCharacters;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
