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

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.plugin.angularjs.completion.dto.AngularTemplate;
import com.codenvy.plugin.angularjs.completion.dto.Object;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;

import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse objects
 * @author Florent Benoit
 */
public class ObjectCommentParser implements CodeCommentParser {

    private DtoFactory dtoFactory;

    private AngularTemplate angularTemplate;

    private static final Pattern OBJECT_PATTERN = Pattern.compile("(.*?)\\.(.*)");


    public ObjectCommentParser(DtoFactory dtoFactory, AngularTemplate angularTemplate) {
        this.dtoFactory = dtoFactory;
        this.angularTemplate = angularTemplate;
    }

    @Override
    public void onComment(CommentContext commentContext) {
        // register a new Object for the given provider
        Object object = dtoFactory.createDto(Object.class);
        String objectName = commentContext.getAttributeValue("name");

        // extract object name
        Matcher objectNameMatcher = OBJECT_PATTERN.matcher(objectName);
        if (objectNameMatcher.find()) {
            // get provider from method name
            String providerName = objectNameMatcher.group(1);
            String oName = objectNameMatcher.group(2);

            object.setName(oName);

            TemplateDotProvider templateDotProvider = angularTemplate.getTemplateProvider(providerName);
            if (templateDotProvider == null) {
                templateDotProvider = dtoFactory.createDto(TemplateDotProvider.class);
                templateDotProvider.setName(providerName);
                angularTemplate.addOrGet(templateDotProvider);
            }
            templateDotProvider.getObjects().add(object);

        } else {
            // add complete name

            TemplateDotProvider templateDotProvider = angularTemplate.getTemplateProvider(objectName);
            if (templateDotProvider == null) {
                templateDotProvider = dtoFactory.createDto(TemplateDotProvider.class);
                templateDotProvider.setName(objectName);
                templateDotProvider.setType(getSupportedType().name());
                angularTemplate.addOrGet(templateDotProvider);
            }


        }
    }

    @Override
    public AngularDocType getSupportedType() {
        return AngularDocType.OBJECT;
    }
}
