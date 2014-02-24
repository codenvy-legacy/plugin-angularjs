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

/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.ext.angularjs.client.menu.wizard;

import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.validation.constraints.NotNull;

import static com.codenvy.ide.api.ui.wizard.newresource.NewResourceWizardKeys.NEW_RESOURCE_PROVIDER;
import static com.codenvy.ide.api.ui.wizard.newresource.NewResourceWizardKeys.PARENT;
import static com.codenvy.ide.api.ui.wizard.newresource.NewResourceWizardKeys.PROJECT;
import static com.codenvy.ide.api.ui.wizard.newresource.NewResourceWizardKeys.RESOURCE_NAME;

public class YeomanWizardPresenter extends AbstractWizardPage implements YeomanWizardSelectNameView.ActionDelegate {
    private YeomanWizardSelectNameView view;
    private NewResourceProvider        selectedResourceType;
    private boolean                    isResourceNameValid;
    private boolean                    hasSameResource;
    private Project                    project;
    private Folder                     parent;
    private Project                    treeStructure;

    /**
     * Create presenter.
     *
     * @param view
     */
    @Inject
    public YeomanWizardPresenter(YeomanWizardSelectNameView view) {
        super("Select a name to be generated with this Yeoman generator", null);

        this.view = view;
        this.view.setDelegate(this);

    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompleted() {
        return  !view.getResourceName().isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void focusComponent() {
    }

    /** {@inheritDoc} */
    @Override
    public void removeOptions() {
        wizardContext.removeData(RESOURCE_NAME);
    }

    /** {@inheritDoc} */
    @Override
    public String getNotice() {
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** {@inheritDoc} */
    @Override
    public void onResourceNameChanged() {
        checkEnteredData();
    }

    /**
     * Check the data on the view.
     */
    private void checkEnteredData() {
        String resourceName = view.getResourceName();

        wizardContext.putData(RESOURCE_NAME, resourceName);

        delegate.updateControls();
    }

    /** {@inheritDoc} */
    @Override
    public void commit(@NotNull final CommitCallback callback) {
        callback.toString();
    }

}
