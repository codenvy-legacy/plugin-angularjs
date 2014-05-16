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
