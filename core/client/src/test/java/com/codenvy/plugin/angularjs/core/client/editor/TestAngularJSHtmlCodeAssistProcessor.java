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

import com.codenvy.plugin.angularjs.core.client.editor.AngularJSHtmlCodeAssistProcessor;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSQuery;

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
public class TestAngularJSHtmlCodeAssistProcessor {

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
