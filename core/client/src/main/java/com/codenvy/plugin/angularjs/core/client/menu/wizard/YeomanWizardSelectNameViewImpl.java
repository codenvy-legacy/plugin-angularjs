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

import com.codenvy.plugin.angularjs.core.client.menu.wizard.YeomanWizardSelectNameView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


/**
 * @author Florent Benoit
 */
public class YeomanWizardSelectNameViewImpl extends Composite implements YeomanWizardSelectNameView {

    @Override
    public String getResourceName() {
        return resourceName.getText();
    }

    interface NewResourceViewUiBinder extends UiBinder<Widget, YeomanWizardSelectNameViewImpl> {
    }

    private static NewResourceViewUiBinder uiBinder = GWT.create(NewResourceViewUiBinder.class);

    @UiField
    TextBox resourceName;


    private ActionDelegate delegate;

    /**
     * Create view.
     */
    @Inject
    public YeomanWizardSelectNameViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("resourceName")
    public void onResourceNameKeyUp(KeyUpEvent event) {
        delegate.onResourceNameChanged();
    }
}
