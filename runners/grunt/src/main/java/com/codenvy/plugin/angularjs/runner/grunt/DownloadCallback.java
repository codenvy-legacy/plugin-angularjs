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

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.core.util.DownloadPlugin;
import com.codenvy.api.runner.internal.DeploymentSources;

import java.io.File;
import java.io.IOException;

/**
 * @author Florent Benoit
 */
public class DownloadCallback implements DownloadPlugin.Callback {
    private DeploymentSources resultHolder;

    private IOException errorHolder;


    @Override
    public void done(File downloaded) {
        resultHolder = new DeploymentSources(downloaded);
    }

    @Override
    public void error(IOException e) {
        errorHolder = e;
    }

    public DeploymentSources getResultHolder() {
        return resultHolder;
    }

    public IOException getErrorHolder() {
        return errorHolder;
    }

}
