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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Implementation of the folding panel
 *
 * @author Florent Benoit
 */
public class FoldingPanelImpl extends Composite implements FoldingPanel {

    /**
     * UI binder.
     */
    private static FoldingPanelImplUiBinder uiBinder = GWT.create(FoldingPanelImplUiBinder.class);

    /**
     * The toggle button.
     */
    @UiField
    Label toggleButton;

    /**
     * Inner css.
     */
    @UiField
    FoldingCss style;

    /**
     * Toggle panel.
     */
    @UiField
    FlowPanel togglePanel;

    /**
     * Title of the element
     */
    @UiField
    Label foldingTitle;

    /**
     * State of the folding panel.
     */
    private boolean open = true;

    /**
     * Constructor that is used with Dependency Injection
     *
     * @param name
     *         the title of this panel
     */
    @AssistedInject
    public FoldingPanelImpl(@Assisted String name) {
        super();
        initWidget(uiBinder.createAndBindUi(this));
        setOpen(true);
        this.foldingTitle.setText(name);
    }

    /**
     * Toggle the state.
     */
    protected void toggle() {
        this.open = !this.open;
        setOpen(this.open);
    }

    /**
     * Handle the click on the toggleButton
     *
     * @param event
     */
    @UiHandler("toggleButton")
    public void handleOpenCloseClick(final ClickEvent event) {
        toggle();
    }

    protected void setOpen(final boolean open) {
        if (open) {
            this.togglePanel.removeStyleName(style.folded());
            this.toggleButton.removeStyleName(style.toggleButtonClosed());
        } else {
            this.togglePanel.addStyleName(style.folded());
            this.toggleButton.addStyleName(style.toggleButtonClosed());
        }
    }

    public FlowPanel getTogglePanel() {
        return togglePanel;
    }


    interface FoldingPanelImplUiBinder extends UiBinder<Widget, FoldingPanelImpl> {
    }

    public interface FoldingCss extends CssResource {

        @ClassName("toggleButton-closed")
        String toggleButtonClosed();

        @ClassName("folded")
        String folded();

    }
}
