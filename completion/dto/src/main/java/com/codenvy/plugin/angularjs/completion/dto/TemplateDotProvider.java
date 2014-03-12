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

package com.codenvy.plugin.angularjs.completion.dto;

import com.codenvy.dto.shared.DTO;

import com.codenvy.plugin.angularjs.completion.dto.Object;
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

    void setObjects(List<Object> objects);
    List<Object> getObjects();

    void setEvents(List<Event> events);
    List<Event> getEvents();


}
