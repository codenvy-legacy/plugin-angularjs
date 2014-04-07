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

package com.codenvy.plugin.angularjs.core.client.menu.wizard;

import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Folder;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.builder.client.build.BuildProjectPresenter;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static com.codenvy.ide.api.ui.wizard.newresource.NewResourceWizardKeys.RESOURCE_NAME;

/**
 * @author Florent Benoit
 */
public class YeomanWizardPresenter extends AbstractWizardPage implements YeomanWizardSelectNameView.ActionDelegate {
    private YeomanWizardSelectNameView view;
    private BuildProjectPresenter      buildProjectPresenter;

    private DtoFactory dtoFactory;

    private ResourceProvider resourceProvider;

    /**
     * Create presenter.
     *
     * @param view
     */
    @Inject
    public YeomanWizardPresenter(YeomanWizardSelectNameView view, DtoFactory dtoFactory, BuildProjectPresenter buildProjectPresenter,
                                 ResourceProvider resourceProvider) {
        super("Select a name to be generated with this Yeoman generator", null);

        this.view = view;
        this.view.setDelegate(this);
        this.dtoFactory = dtoFactory;
        this.buildProjectPresenter = buildProjectPresenter;
        this.resourceProvider = resourceProvider;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompleted() {
        return !view.getResourceName().isEmpty();
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

        List<String> targets = Arrays.asList(new String[]{"angular:directive", wizardContext.getData(RESOURCE_NAME)});
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("yeoman");
        buildProjectPresenter.buildActiveProject(buildOptions);


        Timer t = new Timer() {
            public void run() {
                resourceProvider.getActiveProject().refreshChildren(new AsyncCallback<Project>() {
                    @Override
                    public void onSuccess(Project result) {
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                    }
                });
            }
        };

        // Schedule the timer to run once in 5 seconds.
        t.schedule(5000);

    }

}
