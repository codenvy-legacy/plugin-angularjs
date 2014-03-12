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

package com.codenvy.plugin.angularjs.api.server;

import com.codenvy.api.project.shared.ProjectTemplateDescription;

import java.util.List;

/**
 * This interfaces allows to plug new AngularJS templates.
 * Modules implementing this interface need to use for example Singleton
 * @author Florent Benoit
 */
public interface AngularProjectTemplateExtension {

    /**
     * @return a list of templates that can be used for AngularJS
     */
    List<ProjectTemplateDescription> getTemplates();
}
