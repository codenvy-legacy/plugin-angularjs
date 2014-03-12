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

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizard;
import com.google.inject.Inject;

/**
 * @author Florent Benoit
 */
public class YeomanAddDirectiveAction extends Action {

    private BuildProjectPresenter buildProjectPresenter;

    private DtoFactory dtoFactory;

    private WizardDialogFactory wizardDialogFactory;
    private DefaultWizard wizard;
    private NotificationManager notificationManager;

    @Inject
    public YeomanAddDirectiveAction(LocalizationConstant localizationConstant, BuildProjectPresenter buildProjectPresenter, DtoFactory dtoFactory, WizardDialogFactory wizardDialogFactory,
                                    @YeomanWizard DefaultWizard wizard, NotificationManager notificationManager) {
        super(localizationConstant.yeomanAddDirectiveText(), localizationConstant.runAppActionDescription(), null);
        this.buildProjectPresenter = buildProjectPresenter;
        this.dtoFactory = dtoFactory;
        this.wizardDialogFactory = wizardDialogFactory;
        this.wizard = wizard;
        this.notificationManager = notificationManager;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {

        try {

            WizardDialog wizardDialog = wizardDialogFactory.create(wizard);
            wizardDialog.show();
        } catch (Exception e1) {
            String errorMassage = e1.getMessage() != null ? e1.getMessage()
                                                          : "An error occured while creating the datasource connection";
            Notification notification = new Notification(errorMassage, Notification.Type.ERROR);
            notificationManager.showNotification(notification);
        }
    }



}
