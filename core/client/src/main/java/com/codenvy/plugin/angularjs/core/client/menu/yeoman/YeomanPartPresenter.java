/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.core.client.menu.yeoman;

import com.codenvy.ide.api.editor.TextEditorPartPresenter;
import com.codenvy.ide.api.event.ActivePartChangedEvent;
import com.codenvy.ide.api.event.ActivePartChangedHandler;
import com.codenvy.ide.api.parts.base.BasePresenter;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;


/**
 * Presenter that will display the Yeoman panel on the right.
 *
 * @author Florent Benoit
 */
@Singleton
public class YeomanPartPresenter extends BasePresenter implements ActivePartChangedHandler, YeomanPartView.ActionDelegate {

    /**
     * The view of the yeoman panel.
     */
    private final YeomanPartView view;

    /**
     * Current active part.
     */
    private TextEditorPartPresenter activePart;

    @Inject
    public YeomanPartPresenter(YeomanPartView view, EventBus eventBus) {
        this.view = view;
        eventBus.addHandler(ActivePartChangedEvent.TYPE, this);
        view.setTitle("Yeoman");
        view.setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return "Yeoman";
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return "Yeoman Generator";
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivePartChanged(ActivePartChangedEvent event) {
        if (event.getActivePart() instanceof TextEditorPartPresenter) {
            if (activePart != event.getActivePart()) {
                activePart = (TextEditorPartPresenter)event.getActivePart();
                if (activePart.getOutline() != null) {
                    activePart.getOutline().go(view.getContainer());
                } else {
                    view.showNoYeoman();
                }
            }
        }
    }

}
