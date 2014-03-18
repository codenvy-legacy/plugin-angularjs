/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2014] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */

package com.codenvy.plugin.angularjs.core.client.menu;


import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.FileEvent;
import com.codenvy.ide.api.resources.FileEventHandler;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceAgent;
import com.codenvy.ide.extension.runner.client.RunnerController;
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
@Extension(title = "AngularJS Menu extension.", version = "1.0.0")
public class AngularJSMenuExtension {

    private static final String YEOMAN_GROUP_MENU = "YeomanMenu";

    @Inject
    public AngularJSMenuExtension(ActionManager actionManager, ResourceProvider resourceProvider,
                                  LocalizationConstant localizationConstant,
                                  YeomanAddDirectiveAction yeomanAddDirectiveAction,
                                  @YeomanWizard DefaultWizard newResourceWizard,
                                  Provider<YeomanWizardPresenter> chooseResourcePage) {


        newResourceWizard.addPage(chooseResourcePage);


        // Build a new Yeoman menu
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(GROUP_MAIN_MENU);
        DefaultActionGroup yeomanActionGroup = new CustomActionGroup(resourceProvider, "Yeoman", true, actionManager);

        actionManager.registerAction(YEOMAN_GROUP_MENU,
                                     yeomanActionGroup);
        Constraints beforeWindow = new Constraints(BEFORE, GROUP_WINDOW);
        mainMenu.add(yeomanActionGroup, beforeWindow);


        actionManager.registerAction(localizationConstant.yeomanAddDirectiveId(), yeomanAddDirectiveAction);
        yeomanActionGroup.add(yeomanAddDirectiveAction);


    }
}
