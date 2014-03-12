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

package com.codenvy.plugin.angularjs.completion.dto.parser;


import com.codenvy.plugin.angularjs.completion.dto.parser.api.AngularDocType;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CommentContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            this.type = AngularDocType.valueOf(type.toUpperCase());
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
