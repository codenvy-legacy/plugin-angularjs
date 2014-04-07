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

package com.codenvy.plugin.angularjs.completion.dto.parser;


import com.codenvy.plugin.angularjs.completion.dto.parser.api.AngularDocType;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CommentContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Florent Benoit
 */
public class AngularCommentContext implements CommentContext {

    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("@(.*?)\\s(.*?)\n");

    private AngularDocType type;

    private String comment;


    private Map<String, List<String>> attributes;


    public AngularCommentContext(String type, String comment) {
        this.attributes = new HashMap<>();
        try {
            this.type = AngularDocType.valueOf(type.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            this.type = AngularDocType.UNKNOWN;
        }
        this.comment = comment;

        initAttributes();
    }


    protected void initAttributes() {
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(comment);
        while (matcher.find()) {
            String attributeName = matcher.group(1).trim();
            String attributeValue = matcher.group(2);

            List<String> currentList = attributes.get(attributeName);
            if (currentList == null) {
                currentList = new ArrayList<>();
                attributes.put(attributeName, currentList);
            }
            currentList.add(attributeValue);

        }
    }


    public List<String> getAttributeValues(String attributeName) {
        return attributes.get(attributeName);
    }

    public String getAttributeValue(String attributeName) {
        List<String> values = attributes.get(attributeName);
        if (values != null && values.size() > 0) {
            return values.get(0);
        }
        return null;
    }

    public AngularDocType getType() {
        return type;
    }

}
