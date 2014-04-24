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
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.plugin.angularjs.core.client.menu.bower.BowerInstallAction;
import com.codenvy.plugin.angularjs.core.client.menu.npm.NpmInstallAction;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizard;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardPresenter;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import static com.codenvy.ide.api.ui.action.Anchor.BEFORE;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_MAIN_MENU;
import static com.codenvy.ide.api.ui.action.IdeActions.GROUP_WINDOW;


/**
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "AngularJS Menu extension.", version = "3.0.0")
public class AngularJSMenuExtension {

    private static final String YEOMAN_GROUP_MENU = "YeomanMenu";
    private static final String NPM_GROUP_MENU = "NpmMenu";
    private static final String BOWER_GROUP_MENU = "BowerMenu";

    @Inject
    public AngularJSMenuExtension(ActionManager actionManager, ResourceProvider resourceProvider,
                                  LocalizationConstant localizationConstantYeoman,
                                  com.codenvy.plugin.angularjs.core.client.menu.bower.LocalizationConstant localizationConstantBower,
                                  com.codenvy.plugin.angularjs.core.client.menu.npm.LocalizationConstant localizationConstantNpm,
                                  YeomanAddDirectiveAction yeomanAddDirectiveAction,
                                  final NpmInstallAction npmInstallAction,
                                  final BowerInstallAction bowerInstallAction,
                                  @YeomanWizard DefaultWizard newResourceWizard,
                                  Provider<YeomanWizardPresenter> chooseResourcePage,
                                  EventBus eventBus) {


        newResourceWizard.addPage(chooseResourcePage);


        // Register all actions
        actionManager.registerAction(localizationConstantYeoman.yeomanAddDirectiveId(), yeomanAddDirectiveAction);
        actionManager.registerAction(localizationConstantNpm.npmInstallId(), npmInstallAction);
        actionManager.registerAction(localizationConstantBower.bowerInstallId(), bowerInstallAction);

        // Get main menu
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(GROUP_MAIN_MENU);

        // create constraint
        Constraints beforeWindow = new Constraints(BEFORE, GROUP_WINDOW);

        // Build a new Yeoman menu
        DefaultActionGroup yeomanActionGroup = new CustomActionGroup(resourceProvider, "Yeoman", true, actionManager);
        actionManager.registerAction(YEOMAN_GROUP_MENU,
                                     yeomanActionGroup);
        mainMenu.add(yeomanActionGroup, beforeWindow);

        // register actions for Yeoman menu
        yeomanActionGroup.add(yeomanAddDirectiveAction);


        // Build NPM menu
        DefaultActionGroup npmActionGroup = new CustomActionGroup(resourceProvider, "Npm", true, actionManager);
        actionManager.registerAction(NPM_GROUP_MENU,
                                     npmActionGroup);
        mainMenu.add(npmActionGroup, beforeWindow);

        // register actions for Yeoman menu
        npmActionGroup.add(npmInstallAction);


        // Build Bower menu
        DefaultActionGroup bowerActionGroup = new CustomActionGroup(resourceProvider, "Bower", true, actionManager);
        actionManager.registerAction(BOWER_GROUP_MENU,
                                     bowerActionGroup);
        mainMenu.add(bowerActionGroup, beforeWindow);

        // register actions for Bower menu
        bowerActionGroup.add(bowerInstallAction);


        // Install NPM dependencies when projects is being opened and that there is no node_modules directory
        eventBus.addHandler(ProjectActionEvent.TYPE, new ProjectActionHandler() {
            @Override
            public void onProjectOpened(ProjectActionEvent event) {
                Project project = event.getProject();
                // Check if there is package.json file
                Resource packageJsonFile = project.findChildByName("package.json");
                if (packageJsonFile != null) {
                    Resource nodeModulesDirectory = project.findChildByName("node_modules");
                    // NPM configured for the project but not yet initialized ?
                    if (nodeModulesDirectory == null) {
                        // Install npm dependencies
                        npmInstallAction.installDependencies();
                    }
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

            @Override
            public void onProjectClosed(ProjectActionEvent event) {
            }

            @Override
            public void onProjectDescriptionChanged(ProjectActionEvent event) {
            }
        });

    }
}
