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


import com.codenvy.dto.server.DtoFactory;
import com.codenvy.plugin.angularjs.completion.dto.AngularTemplate;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.AngularDocType;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CommentContext;

/**
 * Parse provider
 * @author Florent Benoit
 */
public class ProviderCommentParser implements CodeCommentParser {

    private DtoFactory dtoFactory;

    private AngularTemplate angularTemplate;

    public ProviderCommentParser(DtoFactory dtoFactory, AngularTemplate angularTemplate) {
        this.dtoFactory = dtoFactory;
        this.angularTemplate = angularTemplate;
    }


    @Override
    public void onComment(CommentContext commentContext) {
        String name = commentContext.getAttributeValue("name");
        // register a new Provider

        TemplateDotProvider templateDotProvider = dtoFactory.createDto(TemplateDotProvider.class);
        templateDotProvider.setName(name);
        templateDotProvider.setType(getSupportedType().name());

        // add it if not exist
        angularTemplate.addOrGet(templateDotProvider);

    }

    @Override
    public AngularDocType getSupportedType() {
        return AngularDocType.PROVIDER;
    }

}
