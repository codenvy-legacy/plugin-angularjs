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

import java.util.List;

/**
 * @author Florent Benoit
 */
public class AngularTemplate {

    private Templating templating;

    public AngularTemplate(Templating templating) {
        this.templating = templating;
    }

    public TemplateDotProvider getTemplateProvider(String name) {
        List<TemplateDotProvider> templateDotProviderList = templating.getTemplateDotProviders();
        if (templateDotProviderList != null) {
            for (TemplateDotProvider templateDotProvider : templateDotProviderList) {
                if (name != null && name.equals(templateDotProvider.getName())) {
                    return templateDotProvider;
                }
            }
        }
        return null;
    }


    public Templating getTemplating() {
        return templating;
    }

    public TemplateDotProvider addOrGet(TemplateDotProvider templateDotProvider) {
        TemplateDotProvider existing = getTemplateProvider(templateDotProvider.getName());
        if (existing != null) {
            return existing;
        }
        templating.getTemplateDotProviders().add(templateDotProvider);
        return templateDotProvider;
    }

}
