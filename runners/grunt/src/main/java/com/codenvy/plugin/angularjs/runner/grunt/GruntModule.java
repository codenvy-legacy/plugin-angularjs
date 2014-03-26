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

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.runner.internal.Runner;
import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Module for Grunt  module.
 *
 * @author Florent Benoit
 */
@DynaModule
public class GruntModule extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<Runner> multiBinderRunners = Multibinder.newSetBinder(binder(), Runner.class);
        multiBinderRunners.addBinding().to(GruntRunner.class);

    }
}
