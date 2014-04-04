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

package com.codenvy.plugin.angularjs.template.gulp;

import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.plugin.angularjs.api.server.AngularProjectTemplateExtension;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the Gulp AngularJS starter template for AngularJS project type.
 * @author Florent Benoit
 */
@Singleton
public class GulpAngularStarterTemplateExtension implements AngularProjectTemplateExtension {

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<>(1);

        list.add(new ProjectTemplateDescription("zip",
                                                "AngularJS (Gulp AngularJS starter)",
                                                "Project using gulp and AngularJS scaffolding.",
                                                "templates/gulp-starter.zip"));

        return list;
    }
}
