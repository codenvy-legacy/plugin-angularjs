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

import com.codenvy.api.builder.BuildStatus;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.plugin.angularjs.core.client.builder.BuildFinishedCallback;
import com.codenvy.plugin.angularjs.core.client.builder.BuilderAgent;
import com.codenvy.plugin.angularjs.core.client.editor.AngularJSResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Florent Benoit
 */
public class YeomanPartViewImpl extends BaseView<YeomanPartView.ActionDelegate> implements YeomanPartView, BuildFinishedCallback {

    private static YeomanPartViewImplUiBinder uiBinder = GWT.create(YeomanPartViewImplUiBinder.class);
    /**
     * CSS resource.
     */
    @UiField(provided = true)
    protected final AngularJSResources uiResources;
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
    private DtoFactory dtoFactory;
    private ResourceProvider resourceProvider;
    private BuilderAgent builderAgent;
    private SimplePanel container;
    private Label noYeoman;
    /**
     * Associate a generator type to a list of names to generate.
     */
    private Map<YeomanGeneratorType, List<String>> namesByTypes;
    /**
     * Associate a generator type to a widget
     */
    private Map<YeomanGeneratorType, FoldingPanel> widgetByTypes;
    private FoldingPanelFactory      foldingPanelFactory;
    private GeneratedItemViewFactory generatedItemViewFactory;
    @Inject
    public YeomanPartViewImpl(AngularJSResources angularJSResources, PartStackUIResources resources,
                              FoldingPanelFactory foldingPanelFactory, GeneratedItemViewFactory generatedItemViewFactory,
                              ResourceProvider resourceProvider, DtoFactory dtoFactory, BuilderAgent builderAgent) {
        super(resources);
        this.uiResources = angularJSResources;
        this.foldingPanelFactory = foldingPanelFactory;
        this.generatedItemViewFactory = generatedItemViewFactory;
        this.resourceProvider = resourceProvider;
        this.dtoFactory = dtoFactory;
        this.builderAgent = builderAgent;
        this.namesByTypes = new HashMap<>();
        this.widgetByTypes = new HashMap<>();

        noYeoman = new Label("Yeoman is not available.");
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

        updateExecuteButton();

    }

    public static String joinList(List list) {
        return list.toString().replaceAll("[\\[.\\].\\s+]", "");
    }

    @UiHandler("generateButton")
    public void clickOnGenerateButton(final ClickEvent event) {
        List<String> targets = new ArrayList<>();

        // Now add all the generated case
        for (Map.Entry<YeomanGeneratorType, List<String>> entry : namesByTypes.entrySet()) {
            YeomanGeneratorType type = entry.getKey();
            List<String> names = entry.getValue();

            targets.add("angular:".concat(type.getName().toLowerCase()));
            targets.add(joinList(names));
        }

        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("yeoman");
        builderAgent.build(buildOptions, "Using Yeoman generator...", "The Yeoman generator has finished",
                           "Failed to use Yeoman generator", "yeoman", this);

        // disable the button
        generateButton.setEnabled(false);

    }

    @UiHandler("addButton")
    public void clickOnAddButton(final ClickEvent event) {

        String generatedName = resourceName.getText();
        // entry name not empty?
        if (generatedName == null || "".equals(generatedName)) {
            return;
        }

        // There is an entry, now check the type
        String type = resourceType.getValue(resourceType.getSelectedIndex());
        YeomanGeneratorType selectedType = null;
        for (YeomanGeneratorType yeomanGeneratorType : YeomanGeneratorType.values()) {
            if (yeomanGeneratorType.getName().equalsIgnoreCase(type)) {
                selectedType = yeomanGeneratorType;
                break;
            }
        }

        // nothing selected
        if (selectedType == null) {
            return;
        }

        // existing values ?
        List<String> generatedNames = namesByTypes.get(selectedType);
        if (generatedNames == null) {
            // needs to add also the widget
            FoldingPanel foldingPanel = foldingPanelFactory.create(selectedType.getLabelName());
            widgetByTypes.put(selectedType, foldingPanel);
            resultZone.add(foldingPanel);
            generatedNames = new ArrayList<>();
            namesByTypes.put(selectedType, generatedNames);
        }


        // check if already exists
        if (generatedNames.contains(generatedName)) {
            return;
        }

        // needs to add element
        generatedNames.add(generatedName);

        // Also create the widget
        GeneratedItemView item = generatedItemViewFactory.create(generatedName, selectedType, uiResources);
        item.setAnchor(this);
        // add it in the right parent
        widgetByTypes.get(selectedType).getTogglePanel().add(item);

        updateExecuteButton();


    }

    /** {@inheritDoc} */
    @Override
    public void showNoYeoman() {
        container.clear();
        container.add(noYeoman);
    }

    @Override
    public void removeItem(YeomanGeneratorType type, String name, GeneratedItemView itemView) {
        // get names
        List<String> existingNames = namesByTypes.get(type);

        // contains element to remove
        if (existingNames != null && existingNames.contains(name)) {
            existingNames.remove(name);
            // No more items, remove the widget
            if (existingNames.isEmpty()) {
                namesByTypes.remove(type);
                FoldingPanel previous = widgetByTypes.remove(type);
                resultZone.remove(previous);
            } else {
                // remove the entry
                FoldingPanel selectedPanel = widgetByTypes.get(type);
                selectedPanel.getTogglePanel().remove(itemView);
            }

        }

        updateExecuteButton();

    }

    /**
     * Disable or enable the button if there are items to generate
     */
    protected void updateExecuteButton() {
        generateButton.setEnabled(!namesByTypes.isEmpty());
    }

    /** {@inheritDoc} */
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }

    @Override
    public void onFinished(BuildStatus buildStatus) {
        // refresh the tree if it is successful
        if (buildStatus == BuildStatus.SUCCESSFUL) {
            resourceProvider.getActiveProject().refreshChildren(new AsyncCallback<Project>() {
                @Override
                public void onSuccess(Project result) {
                    // remove what has been generated
                    namesByTypes.clear();
                    widgetByTypes.clear();
                    resultZone.clear();
                    updateExecuteButton();
                }

                @Override
                public void onFailure(Throwable caught) {
                    updateExecuteButton();
                }
            });
        }

    }


    interface YeomanPartViewImplUiBinder extends UiBinder<Widget, YeomanPartViewImpl> {
    }
}
