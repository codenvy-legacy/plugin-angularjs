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

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Interface of the view panel that will manage the yeoman generator view.
 */
public interface YeomanPartView extends View<YeomanPartView.ActionDelegate> {

    void setTitle(String title);

    void removeItem(YeomanGeneratorType type, String name, GeneratedItemView generatedItemView);

    void clear();

    /**
     * Enable the button
     */
    void enableGenerateButton();

    /**
     * Disable the generate button
     */
    void disableGenerateButton();

    /**
     * Disable spinner on the generate button
     */
    void disableProgressOnGenerateButton();

    /**
     * Enable spinner on the generate button
     */
    void enableProgressOnGenerateButton();


    void addFoldingPanel(FoldingPanel foldingPanel);
    void removeFoldingPanel(FoldingPanel foldingPanel);


    public interface ActionDelegate extends BaseActionDelegate {

        void addItem(String name, YeomanGeneratorType type);

        void generate();

        void removeItem(YeomanGeneratorType type, String name, GeneratedItemView itemView);

    }
}
