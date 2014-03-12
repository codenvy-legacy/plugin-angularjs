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

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.plugin.angularjs.completion.dto.AngularTemplate;
import com.codenvy.plugin.angularjs.completion.dto.Event;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;

import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse events
 * @author Florent Benoit
 */
public class EventCommentParser implements CodeCommentParser {

    private DtoFactory dtoFactory;

    private AngularTemplate angularTemplate;

    private static final Pattern EVENT_PATTERN = Pattern.compile("(.*?)#(.*)");


    public EventCommentParser(DtoFactory dtoFactory, AngularTemplate angularTemplate) {
        this.dtoFactory = dtoFactory;
        this.angularTemplate = angularTemplate;
    }

    @Override
    public void onComment(CommentContext commentContext) {
        // Create a new event
        Event event = dtoFactory.createDto(Event.class);

        String eventName = commentContext.getAttributeValue("name");

        // extract method name
        Matcher methodNameMatcher = EVENT_PATTERN.matcher(eventName);
        if (methodNameMatcher.find()) {
            // get provider from method name
            String directiveName = methodNameMatcher.group(1);
            String eName = methodNameMatcher.group(2);

            event.setName(eName);

            TemplateDotProvider templateDotProvider = angularTemplate.getTemplateProvider(directiveName);
            if (templateDotProvider != null) {
                templateDotProvider.getEvents().add(event);
            }

        }


    }

    @Override
    public AngularDocType getSupportedType() {
        return AngularDocType.EVENT;
    }
}
