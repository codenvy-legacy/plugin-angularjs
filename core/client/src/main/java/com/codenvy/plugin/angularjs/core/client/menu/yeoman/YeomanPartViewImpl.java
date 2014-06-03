/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.angularjs.core.client.menu.yeoman;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.notification.NotificationResources;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.vectomatic.dom.svg.ui.SVGImage;

/**
 * @author Florent Benoit
 */
public class YeomanPartViewImpl extends BaseView<YeomanPartView.ActionDelegate> implements YeomanPartView {

    private static YeomanPartViewImplUiBinder uiBinder = GWT.create(YeomanPartViewImplUiBinder.class);
    /**
     * CSS resource.
     */
    @UiField(provided = true)
    protected final AngularJSResources uiResources;

    /**
     * CSS resource.
     */
    protected final NotificationResources notificationResources;


    @UiField
    TextBox resourceName;
    @UiField
    ListBox resourceType;
    @UiField
    Button addButton;
    @UiField
    Button generateButton;
    @UiField
    FlowPanel resultZone;

    SimplePanel iconField;

    private SimplePanel container;

    private SVGImage progressIcon;

    @Inject
    public YeomanPartViewImpl(AngularJSResources angularJSResources, PartStackUIResources resources, NotificationResources notificationResources) {
        super(resources);
        this.uiResources = angularJSResources;
        this.notificationResources = notificationResources;

        container = new SimplePanel();
        super.container.add(container);
        container.add(uiBinder.createAndBindUi(this));

        // add types
        resourceType.addItem("Controller");
        resourceType.addItem("Constant");
        resourceType.addItem("Directive");
        resourceType.addItem("Decorator");
        resourceType.addItem("Factory");
        resourceType.addItem("Filter");
        resourceType.addItem("Provider");
        resourceType.addItem("Route");
        resourceType.addItem("Service");
        resourceType.addItem("Value");
        resourceType.addItem("View");

        minimizeButton.ensureDebugId("outline-minimizeBut");

        // add iconPanel inside the button
        iconField = new SimplePanel();
        iconField.setStyleName(angularJSResources.uiCss().yeomanWizardGenerateButtonIcon());
        generateButton.getElement().appendChild(iconField.getElement());

        // create icon
        progressIcon = new SVGImage(notificationResources.progress());
        progressIcon.getElement().setAttribute("class", notificationResources.notificationCss().progress());

        disableGenerateButton();

    }

    @UiHandler("generateButton")
    public void clickOnGenerateButton(final ClickEvent event) {
         delegate.generate();

    }

    @UiHandler("addButton")
    public void clickOnAddButton(final ClickEvent event) {
        String type = resourceType.getValue(resourceType.getSelectedIndex());
        YeomanGeneratorType selectedType = null;
        for (YeomanGeneratorType yeomanGeneratorType : YeomanGeneratorType.values()) {
            if (yeomanGeneratorType.getName().equalsIgnoreCase(type)) {
                selectedType = yeomanGeneratorType;
                break;
            }
        }

        delegate.addItem(resourceName.getText(), selectedType);


    }

    public void clear() {
        resultZone.clear();
    }

    @Override
    public void removeItem(YeomanGeneratorType type, String name, GeneratedItemView itemView) {
        delegate.removeItem(type, name, itemView);
    }

    public void addFoldingPanel(FoldingPanel foldingPanel) {
        resultZone.add(foldingPanel);
    }

    @Override
    public void removeFoldingPanel(FoldingPanel foldingPanel) {
        resultZone.remove(foldingPanel);
    }

    /**
     * Enable the button
     */
    public void enableGenerateButton() {
        generateButton.setEnabled(true);
    }

    /**
     * Disable the generate button
     */
    public void disableGenerateButton() {
        generateButton.setEnabled(false);
    }

    @Override
    public void disableProgressOnGenerateButton() {
        iconField.remove(progressIcon);
    }

    @Override
    public void enableProgressOnGenerateButton() {
        iconField.setWidget(progressIcon);
    }


    interface YeomanPartViewImplUiBinder extends UiBinder<Widget, YeomanPartViewImpl> {
    }

}
