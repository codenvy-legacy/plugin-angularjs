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
package com.codenvy.plugin.angularjs.core.client.menu;


import com.codenvy.ide.api.event.ProjectActionEvent;
import com.codenvy.ide.api.event.ProjectActionHandler;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Folder;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.resources.model.Resource;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.extension.builder.client.BuilderLocalizationConstant;
import com.codenvy.plugin.angularjs.core.client.menu.bower.BowerInstallAction;
import com.codenvy.plugin.angularjs.core.client.menu.npm.NpmInstallAction;
import com.codenvy.plugin.angularjs.core.client.menu.yeoman.YeomanPartPresenter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import static com.codenvy.ide.api.ui.action.Anchor.AFTER;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_BUILD;


/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS Menu extension.", version = "3.0.0")
public class AngularJSMenuExtension {


    @Inject
    public AngularJSMenuExtension(ActionManager actionManager, ResourceProvider resourceProvider,
                                  BuilderLocalizationConstant builderLocalizationConstant,
                                  com.codenvy.plugin.angularjs.core.client.menu.bower.LocalizationConstant localizationConstantBower,
                                  com.codenvy.plugin.angularjs.core.client.menu.npm.LocalizationConstant localizationConstantNpm,
                                  final NpmInstallAction npmInstallAction,
                                  final BowerInstallAction bowerInstallAction,
                                  final YeomanPartPresenter yeomanPartPresenter,
                                  final WorkspaceAgent workspaceAgent,
                                  EventBus eventBus) {



        // Register all actions
        actionManager.registerAction(localizationConstantNpm.npmInstallId(), npmInstallAction);
        actionManager.registerAction(localizationConstantBower.bowerInstallId(), bowerInstallAction);

        // Get Build menu
        DefaultActionGroup buildMenuActionGroup = (DefaultActionGroup)actionManager.getAction(GROUP_BUILD);

        // create constraint
        Constraints afterBuildConstraints = new Constraints(AFTER, builderLocalizationConstant.buildProjectControlId());

        // Add NPM in build menu
        buildMenuActionGroup.add(npmInstallAction, afterBuildConstraints);

        // Add Bower in build menu
        buildMenuActionGroup.add(bowerInstallAction, afterBuildConstraints);


        // Install NPM dependencies when projects is being opened and that there is no node_modules directory
        eventBus.addHandler(ProjectActionEvent.TYPE, new ProjectActionHandler() {
            @Override
            public void onProjectOpened(ProjectActionEvent event) {

                Project project = event.getProject();
                final String projectTypeId = project.getDescription().getProjectTypeId();
                boolean isAngularJSProject = "AngularJS".equals(projectTypeId);
                if (isAngularJSProject) {
                    // add Yeoman panel
                    workspaceAgent.openPart(yeomanPartPresenter, PartStackType.TOOLING);
                    workspaceAgent.hidePart(yeomanPartPresenter);
                }

                // Check if there is bower.json file
                Resource bowerJsonFile = project.findChildByName("bower.json");
                if (bowerJsonFile != null) {
                    Resource appDirectory = project.findChildByName("app");
                    if (appDirectory != null && appDirectory instanceof Folder) {
                        // Bower configured for the project but not yet initialized ?
                        Resource bowerComponentsDirectory = ((Folder) appDirectory).findChildByName("bower_components");
                        if (bowerComponentsDirectory == null) {
                            // Install bower dependencies as the folder doesn't exist
                            bowerInstallAction.installDependencies();
                        }
                    }
                }

            }

            /**
             * Remove Yeoman panel when closing the project if this panel is displayed.
             * @param event the project event
             */
            @Override
            public void onProjectClosed(ProjectActionEvent event) {
                Project project = event.getProject();
                final String projectTypeId = project.getDescription().getProjectTypeId();
                boolean isAngularJSProject = "AngularJS".equals(projectTypeId);
                if (isAngularJSProject) {
                    workspaceAgent.removePart(yeomanPartPresenter);
                }

            }

            @Override
            public void onProjectDescriptionChanged(ProjectActionEvent event) {
            }
        });

    }
}
