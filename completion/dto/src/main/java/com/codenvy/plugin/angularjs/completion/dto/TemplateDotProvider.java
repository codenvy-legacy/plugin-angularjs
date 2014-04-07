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

package com.codenvy.plugin.angularjs.completion.dto;

import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * @author Florent Benoit
 */
@DTO
public interface TemplateDotProvider {

    void setName(String name);
    String getName();

    void setType(String type);
    String getType();

    void setConstructors(List<Param> args);
    List<Param> getConstructors();

    void setMethods(List<Method> methods);
    List<Method> getMethods();

    void setFunctions(List<Function> functions);
    List<Function> getFunctions();

    void setObjects(List<NgObject> ngObjects);
    List<NgObject> getObjects();

    void setEvents(List<Event> events);
    List<Event> getEvents();


}
