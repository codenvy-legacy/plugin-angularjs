package com.codenvy.ide.ext.angularjs.client.menu;

import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.angularjs.client.menu.wizard.YeomanWizard;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;

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
        // Get the result through a prompt
        String directiveName = "toto";

        //build("angular:directive", directiveName);
    }



    protected void build(String... parameters) {
        List<String> targets = Arrays.asList(parameters);
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("yeoman");
        buildProjectPresenter.buildActiveProject(buildOptions);
    }
}
