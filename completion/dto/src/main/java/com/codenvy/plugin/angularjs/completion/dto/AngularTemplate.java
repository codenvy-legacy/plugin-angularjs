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
