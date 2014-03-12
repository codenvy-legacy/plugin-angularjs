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
 * YeomanWizardSelectNameViewImpl is the view of {@link YeomanWizardPresenter}. Provides selecting type of resource for creating new resource.
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
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
