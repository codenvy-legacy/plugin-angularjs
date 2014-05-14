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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.util.AbstractTrie;

/**
 * Holder of all possible AngularJS attributes.
 *
 * @author Florent Benoit
 */
public class AngularJSTrie {
    private static final Array<String> ELEMENTS = Collections.createArray(
            "ng-app",//
            "ng-bind",//
            "ng-bindHtml",//
            "ng-bindTemplate",//
            "ng-blur",//
            "ng-change",//
            "ng-checked",//
            "ng-class",//
            "ng-classEven",//
            "ng-classOdd",//
            "ng-click",//
            "ng-cloak",//
            "ng-controller",//
            "ng-copy",//
            "ng-csp",//
            "ng-cut",//
            "ng-dblclick",//
            "ng-disabled",//
            "ng-focus",//
            "ng-form",//
            "ng-hide",//
            "ng-href",//
            "ng-if",//
            "ng-include",//
            "ng-init",//
            "ng-keydown",//
            "ng-keypress",//
            "ng-keyup",//
            "ng-list",//
            "ng-model",//
            "ng-mousedown",//
            "ng-mouseenter",//
            "ng-mouseleave",//
            "ng-mousemove",//
            "ng-mouseover",//
            "ng-mouseup",//
            "ng-nonBindable",//
            "ng-open",//
            "ng-paste",//
            "ng-pluralize",//
            "ng-readonly",//
            "ng-repeat",//
            "ng-selected",//
            "ng-show",//
            "ng-src",//
            "ng-srcset",//
            "ng-style",//
            "ng-submit",//
            "ng-switch",//
            "ng-transclude",//
            "ng-value"
                                                                         );


    private static final AbstractTrie<AngularJSCompletionProposal> angularJSTrie = createTrie();


    private static AbstractTrie<AngularJSCompletionProposal> createTrie() {
        AbstractTrie<AngularJSCompletionProposal> result = new AbstractTrie<>();
        for (String name : ELEMENTS.asIterable()) {
            result.put(name, new AngularJSCompletionProposal(name));
        }
        return result;
    }

    /**
     * Search available completions and filter out the existing attributes name
     *
     * @param query
     *         the request query
     * @return an array of autocompletions, or an empty array if there are no
     * autocompletion proposals
     */
    public static Array<AngularJSCompletionProposal> findAndFilterAutocompletions(AngularJSQuery query) {
        // use tolower case
        String prefix = query.getPrefix();

        // search attributes
        Array<AngularJSCompletionProposal> searchedProposals = angularJSTrie.search(prefix);

        // Filter out the existing attributes that may be present in the HTML element
        Array<AngularJSCompletionProposal> result = Collections.createArray();
        for (AngularJSCompletionProposal proposal : searchedProposals.asIterable()) {
            if (!query.getExistingAttributes().contains(proposal.getName())) {
                result.add(proposal);
            }
        }
        return result;


    }
}
