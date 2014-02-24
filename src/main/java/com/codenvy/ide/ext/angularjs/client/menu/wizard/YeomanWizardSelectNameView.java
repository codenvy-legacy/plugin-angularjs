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

import com.codenvy.ide.api.mvp.View;

import javax.validation.constraints.NotNull;

/**
 * Interface of new resource view.
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
public interface YeomanWizardSelectNameView extends View<YeomanWizardSelectNameView.ActionDelegate> {
    /** Required for delegating function into NewResource view. */
    public interface ActionDelegate {

        /** Performs some actions in response to a user's changing a resource name. */
        void onResourceNameChanged();
        
    }

    /** @return resource name */
    @NotNull
    String getResourceName();

}