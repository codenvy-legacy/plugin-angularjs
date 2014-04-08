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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.web.html.editor.HTMLCodeAssistProcessor;
import com.codenvy.ide.ext.web.js.editor.JsCodeAssistProcessor;
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

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

/**
 * @author Florent Benoit
 */
public class JavaScriptCodeAssistProcessor implements JsCodeAssistProcessor {

    private static final char ACTIVATION_CHARACTER = '.';

    private boolean isTextToCompleteBeforeDot;

    private native JavaScriptContentAssistProvider getProvider()/*-{
        return $wnd.jsEsprimaContentAssistProvider;
    }-*/;

    private JavaScriptContentAssistProvider provider;

    private JavaScriptResources javaScriptResources;

    private Templating templating;

    private AbstractTrie<TemplateDotProvider> trie;

    @Inject
    public JavaScriptCodeAssistProcessor(DtoFactory dtoFactory, JavaScriptResources javaScriptResources) {
        this.templating = dtoFactory.createDtoFromJson(javaScriptResources.completionTemplatingJson().getText(), Templating.class);
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
                result.sort(String.CASE_INSENSITIVE_ORDER);
                for (String st : result.asIterable()) {
                    TemplateProposal templateProposal = new TemplateProposal(templatePrefix, st, prefixVal.concat(".").concat(st), offset, javaScriptResources);
                    templateProposal.setMethod();
                    prop.add(templateProposal);
                }
            } else if (dot == -1) {
             // Perform completion only if there is no dot
            Array<TemplateDotProvider> result = trie.search(prefix);
            result.sort(new Comparator<TemplateDotProvider>() {
                @Override
                public int compare(TemplateDotProvider o1, TemplateDotProvider o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (TemplateDotProvider provider : result.asIterable()) {
                prop.add(new TemplateProposal(templatePrefix, provider.getName(), provider.getName(), offset, javaScriptResources));
            }


        }


        try {
            JsArray<JsProposal> jsProposals = provider.computeProposals(view.getDocument().get(), offset, c);
            if (jsProposals != null && jsProposals.length() != 0) {
                for (int i = 0; i < jsProposals.length(); i++) {
                    JsProposal jsProposal = jsProposals.get(i);
                    CompletionProposal proposal = new JavaScriptProposal(prefix, jsProposal, offset, javaScriptResources);
                    prop.add(proposal);
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

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
                        break;
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
        return new char[] {ACTIVATION_CHARACTER};
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
