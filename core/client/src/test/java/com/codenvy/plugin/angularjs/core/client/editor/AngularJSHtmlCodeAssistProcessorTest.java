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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test of the code assist processor.
 *
 * @author Florent Benoit
 */
public class AngularJSHtmlCodeAssistProcessorTest {

    @Test
    public void testAngularAttributes() {

        AngularJSHtmlCodeAssistProcessor codeAssistProcessor = new AngularJSHtmlCodeAssistProcessor(null);

        // test space split
        List<String> attributes = codeAssistProcessor.getAngularAttributes("my attributes", false);
        assertNotNull(attributes);
        assertTrue(Arrays.equals(new String[]{"my", "attributes"}, attributes.toArray()));

        // test keep only attribute name
        attributes = codeAssistProcessor.getAngularAttributes("att1=\"val1\" att2=\"val2\"", false);
        assertNotNull(attributes);
        assertTrue(Arrays.equals(new String[]{"att1", "att2"}, attributes.toArray()));

        // test skip last
        attributes = codeAssistProcessor.getAngularAttributes("body att2", true);
        assertNotNull(attributes);
        assertTrue(Arrays.equals(new String[]{"body"}, attributes.toArray()));


    }


    @Test
    public void testGenerateQueryPrefix() {

        AngularJSHtmlCodeAssistProcessor codeAssistProcessor = new AngularJSHtmlCodeAssistProcessor(null);

        AngularJSQuery query = codeAssistProcessor.getQuery("body", " ");
        assertEquals("body", query.getPrefix());
        assertTrue(Arrays.equals(new String[]{}, query.getExistingAttributes().toArray()));

        query = codeAssistProcessor.getQuery("body ", "att1=\"val1\" att2=\"val2\"");
        assertEquals("", query.getPrefix());
        assertTrue(Arrays.equals(new String[]{"body", "att1", "att2"}, query.getExistingAttributes().toArray()));

        query = codeAssistProcessor.getQuery("body att1", "att2=\"val2\"");
        assertEquals("att1", query.getPrefix());
        assertTrue(Arrays.equals(new String[]{"body", "att2"}, query.getExistingAttributes().toArray()));

    }
}
