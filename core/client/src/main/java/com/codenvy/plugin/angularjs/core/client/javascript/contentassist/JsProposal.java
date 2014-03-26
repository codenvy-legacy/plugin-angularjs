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

package com.codenvy.plugin.angularjs.core.client.javascript.contentassist;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Florent Benoit
 */
public final class JsProposal extends JavaScriptObject {

    /**
     *
     */
    protected JsProposal() {
    }

    public native String getProposal()/*-{
        return this.proposal;
    }-*/;

    public native String getDescription()/*-{
        return this.description;
    }-*/;

    public native int getEscapePosition()/*-{
        return this.escapePosition ? this.escapePosition : -1;
    }-*/;

    public native Position[] getPositions()/*-{
        return this.positions ? this.positions : [];
    }-*/;

}
