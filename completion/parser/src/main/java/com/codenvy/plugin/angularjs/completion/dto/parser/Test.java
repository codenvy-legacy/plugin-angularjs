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

                angularJSDocParser.parse(new File("/Users/benoitf/Documents/git/GitHub-PG/codenvy-angularjs-parser/ang-git-src").getCanonicalFile().toPath());
        //angularJSDocParser.parse(new File("/Users/benoitf/Documents/git/GitHub-PG/codenvy-angularjs-parser/target/").getCanonicalFile().toPath());


        // Now write the JSON file
        FileWriter fileWriter = new FileWriter("/Users/benoitf/Documents/git/GitHub-PG/codenvy-angularjs-parser/templates.json");
        fileWriter.write(dtoFactory.toJson(templating));
        fileWriter.close();


    }
}
