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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.api.text.BadLocationException;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.jseditor.client.codeassist.CodeAssistCallback;
import com.codenvy.ide.jseditor.client.codeassist.CompletionProposal;
import com.codenvy.ide.jseditor.client.document.EmbeddedDocument;
import com.codenvy.ide.jseditor.client.text.LinearRange;
import com.codenvy.ide.jseditor.client.text.TextPosition;
import com.codenvy.ide.jseditor.client.texteditor.TextEditor;
import com.codenvy.plugin.angularjs.completion.dto.Method;
import com.codenvy.plugin.angularjs.completion.dto.Param;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;
import com.codenvy.plugin.angularjs.completion.dto.Templating;
import com.codenvy.plugin.angularjs.completion.dto.client.DtoClientImpls;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.ContextFactory;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.IContentAssistProvider;
import com.codenvy.plugin.angularjs.core.client.javascript.contentassist.IContext;

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
    private TextEditor textEditor;

    @Mock
    private JavaScriptCodeAssistProcessor javaScriptCodeAssistProcessor;

    @Mock
    private CodeAssistCallback codeAssistCallback;

    @Mock
    private ContextFactory contextFactory;

    @Mock
    private IContext context;

    @Mock
    private EmbeddedDocument document;

    @Mock
    private IContentAssistProvider provider;


    @Captor
    ArgumentCaptor<List<CompletionProposal>> callbackCompletionProposals;

    /**
     * Setup
     */
    @Before
    public void setUp() throws BadLocationException {
        this.javaScriptCodeAssistProcessor = new JavaScriptCodeAssistProcessor();
        Templating templating = DtoClientImpls.TemplatingImpl.make();
        // populate
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

        doReturn(document).when(textEditor).getDocument();
        doReturn(context).when(contextFactory).create();


    }

    @Test
    public void testSimpleCompletion() throws BadLocationException {
        checkBasicCompletion("$", new TextPosition(0, 1), LinearRange.createWithStart(0).andEnd(1));
    }

    @Test
    public void testSimpleCompletionWithSpacesBefore() throws BadLocationException {
        checkBasicCompletion("          $", new TextPosition(0, 10), LinearRange.createWithStart(0).andEnd(10));
    }

    protected void checkBasicCompletion(String line, TextPosition position, LinearRange lineRange) throws BadLocationException {
        doReturn(line).when(document).getLineContent(anyInt());
        doReturn(position).when(document).getPositionFromIndex(anyInt());
        doReturn(lineRange).when(document).getLinearRangeForLine(anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditor, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        List<CompletionProposal> proposals = callbackCompletionProposals.getValue();


        // 2 proposals
        assertEquals(2, proposals.size());

        assertEquals("$http", proposals.get(0).getDisplayString());
        assertEquals("$route", proposals.get(1).getDisplayString());

    }


    @Test
    public void testSimpleCompletionWithDotPrefix() throws BadLocationException {
        checkCompletionDotWithParams("$http.", new TextPosition(0, 5), LinearRange.createWithStart(0).andLength(5));
    }

    @Test
    public void testSimpleCompletionWithDotPrefixNoParams() throws BadLocationException {
        final String line = "$route.";
        checkCompletionDotWithoutParams(line, new TextPosition(0, line.length()), LinearRange.createWithStart(0).andLength(line.length()));
    }


    @Test
    public void testSimpleCompletionWithDotPrefixAndSpacesWithParams() throws BadLocationException {
        final String line = "      $http.";
        checkCompletionDotWithParams(line, new TextPosition(0, line.length()), LinearRange.createWithStart(0).andLength(line.length()));
    }

    @Test
    public void testSimpleCompletionWithDotPrefixAndSpacesNoParams() throws BadLocationException {
        final String line = "      $route.";
        checkCompletionDotWithoutParams(line, new TextPosition(0, line.length()), LinearRange.createWithStart(0).andLength(line.length()));
    }

    protected void checkCompletionDotWithParams(String line, TextPosition position, LinearRange lineRange) throws BadLocationException {
        doReturn(line).when(document).getLineContent(anyInt());
        doReturn(position).when(document).getPositionFromIndex(anyInt());
        doReturn(lineRange).when(document).getLinearRangeForLine(anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditor, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        List<CompletionProposal> proposals = callbackCompletionProposals.getValue();

        // 2 proposals
        assertEquals(2, proposals.size());

        assertEquals("delete(url,config)", proposals.get(0).getDisplayString());
        assertEquals("get(url,config)", proposals.get(1).getDisplayString());
    }

    protected void checkCompletionDotWithoutParams(String line, TextPosition position, LinearRange lineRange) throws BadLocationException {
        doReturn(line).when(document).getLineContent(anyInt());
        doReturn(position).when(document).getPositionFromIndex(anyInt());
        doReturn(lineRange).when(document).getLinearRangeForLine(anyInt());
        javaScriptCodeAssistProcessor.computeCompletionProposals(textEditor, line.length(), codeAssistCallback);
        verify(codeAssistCallback).proposalComputed(callbackCompletionProposals.capture());

        List<CompletionProposal> proposals = callbackCompletionProposals.getValue();

        // 1 proposals
        assertEquals(1, proposals.size());

        assertEquals("reload()", proposals.get(0).getDisplayString());
    }

    @Test
    public void checkErrorMessage() {
        assertNull(javaScriptCodeAssistProcessor.getErrorMessage());
    }


}
