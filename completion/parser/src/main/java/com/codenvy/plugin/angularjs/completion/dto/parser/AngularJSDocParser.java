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

import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AngularJSDocParser {

    private List<CodeCommentParser> callbacks;

    public AngularJSDocParser() {
        this.callbacks = new ArrayList<CodeCommentParser>();
    }

    public void parse(Path path) throws IOException {
        ParseFile parseFile = new ParseFile();
        for (CodeCommentParser codeCommentParser : callbacks) {
            parseFile.addCodeCommentParser(codeCommentParser);
        }
        Files.walkFileTree(path, parseFile);
    }


    public void addCodeCommentParser(CodeCommentParser codeCommentParser) {
        this.callbacks.add(codeCommentParser);
    }


}