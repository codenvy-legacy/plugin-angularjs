/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.core.client.menu.yeoman;

/**
 * Allow to build folding panel based on the given name
 *
 * @author Florent Benoit
 */
public interface FoldingPanelFactory {

    /**
     * Creates a new folding panel with the given name
     *
     * @param name
     *         the given name
     * @return the created widget
     */
    FoldingPanel create(String name);
}
