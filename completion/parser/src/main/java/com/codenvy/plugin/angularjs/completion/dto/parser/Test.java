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
import com.codenvy.plugin.angularjs.completion.dto.Templating;
import com.codenvy.plugin.angularjs.completion.dto.server.DtoServerImpls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Florent Benoit
 */
public class Test {

    public static void main(String[] args) throws IOException {
        AngularJSDocParser angularJSDocParser = new AngularJSDocParser();


        DtoFactory dtoFactory = DtoFactory.getInstance();
        DtoServerImpls dtoServerImpls = new DtoServerImpls();
        dtoServerImpls.accept(dtoFactory);

        Templating templating = dtoFactory.createDto(Templating.class);

        AngularTemplate angularTemplate = new AngularTemplate(templating);

        ProviderCommentParser providerCommentParser = new ProviderCommentParser(dtoFactory, angularTemplate);
        ServiceCommentParser serviceCommentParser = new ServiceCommentParser(dtoFactory, angularTemplate);
        DirectiveCommentParser directiveCommentParser = new DirectiveCommentParser(dtoFactory, angularTemplate);
        MethodCommentParser methodCommentParser = new MethodCommentParser(dtoFactory, angularTemplate);
        FunctionCommentParser functionCommentParser = new FunctionCommentParser(dtoFactory, angularTemplate);
        EventCommentParser eventCommentParser = new EventCommentParser(dtoFactory, angularTemplate);
        ObjectCommentParser objectCommentParser = new ObjectCommentParser(dtoFactory, angularTemplate);

        angularJSDocParser.addCodeCommentParser(providerCommentParser);
        angularJSDocParser.addCodeCommentParser(directiveCommentParser);
        angularJSDocParser.addCodeCommentParser(serviceCommentParser);
        angularJSDocParser.addCodeCommentParser(methodCommentParser);
        angularJSDocParser.addCodeCommentParser(functionCommentParser);
        angularJSDocParser.addCodeCommentParser(eventCommentParser);
        angularJSDocParser.addCodeCommentParser(objectCommentParser);

        angularJSDocParser.parse(new File("/ang-git-src").getCanonicalFile().toPath());


        // Now write the JSON file
        FileWriter fileWriter = new FileWriter("..../templates.json");
        fileWriter.write(dtoFactory.toJson(templating));
        fileWriter.close();


    }
}
