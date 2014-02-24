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

package com.codenvy.ide.ext.angularjs.client.menu;

import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Florent Benoit
 */
public class YeomanAddDirectiveResourceProvider extends NewResourceProvider {

    private DtoFactory dtoFactory;
    private BuildProjectPresenter buildProjectPresenter;

    @Inject
    public YeomanAddDirectiveResourceProvider(BuildProjectPresenter buildProjectPresenter, DtoFactory dtoFactory) {
        super("AngularJs directive", "AngularJS directive", null, "java");
        this.buildProjectPresenter = buildProjectPresenter;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public void create(@NotNull String name, @NotNull Folder parent, @NotNull Project project, @NotNull AsyncCallback<Resource> callback) {
        build("angular:directive", name);
    }



    protected void build(String... parameters) {
        List<String> targets = Arrays.asList(parameters);
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("yeoman");
        buildProjectPresenter.buildActiveProject(buildOptions);
    }
}
