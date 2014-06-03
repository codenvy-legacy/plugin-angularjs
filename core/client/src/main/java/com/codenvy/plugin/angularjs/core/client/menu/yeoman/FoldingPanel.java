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

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel that will allow to open/hide elements
 *
 * @author Florent Benoit
 */
public interface FoldingPanel extends IsWidget {

    String getName();

    /**
     * Add element
     */
    void add(GeneratedItemView element);

    /**
     * Remove element
     */
    void remove(GeneratedItemView element);


}
