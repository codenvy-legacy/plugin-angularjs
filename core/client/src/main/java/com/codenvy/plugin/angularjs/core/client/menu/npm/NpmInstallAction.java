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

package com.codenvy.plugin.angularjs.core.client.menu.npm;

import com.codenvy.api.builder.BuildStatus;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.plugin.angularjs.core.client.builder.BuildFinishedCallback;
import com.codenvy.plugin.angularjs.core.client.builder.BuilderAgent;
import com.codenvy.plugin.angularjs.core.client.menu.CustomAction;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;

/**
 * Action that install NPM dependencies.
 * @author Florent Benoit
 */
public class NpmInstallAction extends CustomAction implements BuildFinishedCallback {

    private DtoFactory dtoFactory;

    private BuilderAgent builderAgent;

    private ResourceProvider resourceProvider;

    private boolean buildInProgress;

    @Inject
    public NpmInstallAction(LocalizationConstant localizationConstant,
                            DtoFactory dtoFactory, BuilderAgent builderAgent, ResourceProvider resourceProvider) {
        super(resourceProvider, localizationConstant.npmInstallText(), localizationConstant.npmInstallDescription());
        this.dtoFactory = dtoFactory;
        this.builderAgent = builderAgent;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        installDependencies();
    }


    public void installDependencies() {
        buildInProgress = true;
        List<String> targets = Arrays.asList("install");
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("npm");
        builderAgent.build(buildOptions, "Installation of npm dependencies...", "Npm dependencies successfully downloaded",
                           "Npm dependencies install failed", "npm", this);
    }

    @Override
    public void onFinished(BuildStatus buildStatus) {
        // and refresh the tree if success
        if (buildStatus == BuildStatus.SUCCESSFUL) {
        resourceProvider.getActiveProject().refreshChildren(new AsyncCallback<Project>() {
            @Override
            public void onSuccess(Project result) {
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
        }

        // build finished
        buildInProgress = false;

    }


    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(!buildInProgress);
    }
}
