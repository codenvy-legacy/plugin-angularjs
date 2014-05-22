/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.core.client.javascript;

import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CompletionProposal;
import com.codenvy.plugin.angularjs.completion.dto.Method;
import com.codenvy.plugin.angularjs.completion.dto.Param;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;
import com.codenvy.plugin.angularjs.completion.dto.Templating;
import com.codenvy.plugin.angularjs.completion.dto.client.DtoClientImpls;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSHtmlCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.ContextFactory;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.IContentAssistProvider;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.IContext;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.JavaScriptContentAssistProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.codenvy.plugin.angularjs.core.client.javascript.JavaScriptCodeAssistProcessor.ACTIVATION_CHARACTER;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Florent Benoit
 */
@RunWith(MockitoJUnitRunner.class)
public class JavaScriptCodeAssistProcessorTest {

    @Mock
    private DtoFactory dtoFactory;

    @Mock
    private JavaScriptResources resources;

    @Mock
    private TextEditorPartView textEditorPartView;

    @Mock
    private JavaScriptCodeAssistProcessor javaScriptCodeAssistProcessor;

    @Mock
    private CodeAssistCallback codeAssistCallback;

    @Mock
    private ContextFactory contextFactory;

    @Mock
    private IContext context;

    @Mock
    private Document document;

    @Mock
    private Region region;

    @Mock
    private IContentAssistProvider provider;


    @Captor
    ArgumentCaptor<CompletionProposal[]> callbackCompletionProposals;

    /**
     * Setup
     */
    @Before

    public void setUp() throws BadLocationException {
        this.javaScriptCodeAssistProcessor = new JavaScriptCodeAssistProcessor();
        Templating templating = DtoClientImpls.TemplatingImpl.make();
        //populate
        TemplateDotProvider templateDotProvider = DtoClientImpls.TemplateDotProviderImpl.make();
        templateDotProvider.setName("$http");
        templating.getTemplateDotProviders().add(templateDotProvider);

        Param urlParam = DtoClientImpls.ParamImpl.make();
        urlParam.setName("url");
        Param configParam = DtoClientImpls.ParamImpl.make();
        configParam.setName("config");

        Method deleteMethod = DtoClientImpls.MethodImpl.make();
        deleteMethod.setName("delete");
        deleteMethod.getParams().add(urlParam);
        deleteMethod.getParams().add(configParam);

        templateDotProvider.getMethods().add(deleteMethod);
        Method getMethod = DtoClientImpls.MethodImpl.make();
        getMethod.setName("get");
        getMethod.getParams().add(urlParam);
        getMethod.getParams().add(configParam);

        templateDotProvider.getMethods().add(getMethod);

        TemplateDotProvider routeProvider = DtoClientImpls.TemplateDotProviderImpl.make();
        routeProvider.setName("$route");
        templating.getTemplateDotProviders().add(routeProvider);
        Method reloadMethod = DtoClientImpls.MethodImpl.make();
        reloadMethod.setName("reload");
        routeProvider.getMethods().add(reloadMethod);



        javaScriptCodeAssistProcessor.setTemplating(templating);
        javaScriptCodeAssistProcessor.setProvider(provider);
        javaScriptCodeAssistProcessor.setContextFactory(contextFactory);
        javaScriptCodeAssistProcessor.buildTrie();

        doReturn(document).when(textEditorPartView).getDocument();
        doReturn(region).when(document).getLineInformationOfOffset(anyInt());
        doReturn(context).when(contextFactory).create();


    }

    @Test
    public void testSimpleCompletion() throws BadLocationException {
        checkBasicCompletion("$");
    }

    @Test
    public void testSimpleCompletionWithSpacesBefore() throws BadLocationException {
        checkBasicCompletion("          $");
    }

    protected void checkBasicCompletion(String line) throws BadLocationException {
        doReturn(line).when(document).get(anyInt(), anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditorPartView, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        CompletionProposal[] proposals = callbackCompletionProposals.getValue();


        // 2 proposals
        assertEquals(2, proposals.length);

        assertEquals("$http", proposals[0].getDisplayString());
        assertEquals("$route", proposals[1].getDisplayString());

    }


    @Test
    public void testSimpleCompletionWithDotPrefix() throws BadLocationException {
        checkCompletionDotWithParams("$http.");
    }

    @Test
    public void testSimpleCompletionWithDotPrefixNoParams() throws BadLocationException {
        checkCompletionDotWithoutParams("$route.");
    }


    @Test
    public void testSimpleCompletionWithDotPrefixAndSpacesWithParams() throws BadLocationException {
        checkCompletionDotWithParams("      $http.");
    }

    @Test
    public void testSimpleCompletionWithDotPrefixAndSpacesNoParams() throws BadLocationException {
        checkCompletionDotWithoutParams("      $route.");
    }


    protected void checkCompletionDotWithParams(String line)  throws BadLocationException {
        doReturn(line).when(document).get(anyInt(), anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditorPartView, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        CompletionProposal[] proposals = callbackCompletionProposals.getValue();

        // 2 proposals
        assertEquals(2, proposals.length);

        assertEquals("delete(url,config)", proposals[0].getDisplayString());
        assertEquals("get(url,config)", proposals[1].getDisplayString());
    }

    protected void checkCompletionDotWithoutParams(String line)  throws BadLocationException {
        doReturn(line).when(document).get(anyInt(), anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditorPartView, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        CompletionProposal[] proposals = callbackCompletionProposals.getValue();

        // 1 proposals
        assertEquals(1, proposals.length);

        assertEquals("reload()", proposals[0].getDisplayString());
    }


    @Test
    public void checkActivationCharacter() {
        assertArrayEquals(new char[] {ACTIVATION_CHARACTER}, javaScriptCodeAssistProcessor.getCompletionProposalAutoActivationCharacters());
    }

    @Test
    public void checkErrorMessage() {
        assertNull(javaScriptCodeAssistProcessor.getErrorMessage());
    }



}
