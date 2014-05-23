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

package com.codenvy.plugin.angularjs.itests;

import com.codenvy.test.framework.AbstractIntegrationTest;
import com.codenvy.test.framework.selenium.pages.IDEMainPage;

import org.openqa.selenium.support.PageFactory;

/**
 * Check Angular stuff.
 * @author Florent Benoit
 */
public class AngularJSWizard extends AbstractIntegrationTest {

    private IDEMainPage mainPage = null;

    public String access(String url) {
        driver.get(url);
        mainPage = PageFactory.initElements(driver, IDEMainPage.class);
        return "access";
    }


    public String npmMenuIsAvailable() {
        try {
            mainPage.getMainMenuItem("NpmMenu");
            return "is here";
        } catch (Exception e) {
            return "is not here";
        }
    }

    public String bowerMenuIsAvailable() {
        try {
            mainPage.getMainMenuItem("BowerMenu");
            return "is here";
        } catch (Exception e) {
            return "is not here";
        }
    }

    public String yeomanTabIsAvailable() {
        try {
            mainPage.getTab("Yeoman");
            return "is here";
        } catch (Exception e) {
            return "is not here";
        }
    }


}
