/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/

package com.codenvy.plugin.angularjs.core.client.menu.bower;

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
 * Action that install bower dependencies.
 * @author Florent Benoit
 */
public class BowerInstallAction extends CustomAction implements BuildFinishedCallback {

    private DtoFactory dtoFactory;

    private BuilderAgent builderAgent;

    private ResourceProvider resourceProvider;

    private boolean buildInProgress;

    @Inject
    public BowerInstallAction(LocalizationConstant localizationConstant,
                              DtoFactory dtoFactory, BuilderAgent builderAgent, ResourceProvider resourceProvider) {
        super(resourceProvider, localizationConstant.bowerInstallText(), localizationConstant.bowerInstallDescription());
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
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("bower");
        builderAgent.build(buildOptions, "Installation of Bower dependencies...", "Bower dependencies successfully downloaded",
                           "Bower install failed", "bower", this);
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
        buildInProgress = false;


    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(!buildInProgress);
    }


}
