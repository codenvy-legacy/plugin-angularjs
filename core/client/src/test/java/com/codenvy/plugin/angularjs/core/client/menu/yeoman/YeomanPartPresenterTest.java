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
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.plugin.angularjs.core.client.builder.BuilderAgent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.codenvy.plugin.angularjs.core.client.menu.yeoman.YeomanGeneratorType.CONTROLLER;
import static com.codenvy.plugin.angularjs.core.client.menu.yeoman.YeomanGeneratorType.ROUTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test of the Yeoman presenter.
 * @author Florent Benoit
 */
@RunWith(MockitoJUnitRunner.class)
public class YeomanPartPresenterTest {

    private YeomanPartPresenter presenter;

    @Mock
    private YeomanPartView yeomanPartView;

    @Mock
    EventBus eventBus;

    @Mock
    FoldingPanelFactory foldingPanelFactory;

    @Mock
    GeneratedItemViewFactory generatedItemViewFactory;


    @Mock
    ResourceProvider resourceProvider;

    @Mock
    DtoFactory dtoFactory;

    @Mock
    BuildOptions buildOptions;

    @Mock
    BuilderAgent builderAgent;

    @Mock
    ClickEvent clickEvent;

    @Mock
    private FoldingPanel foldingPanelController;

    @Mock
    private FoldingPanel foldingPanelRoute;

    private static final String CONTROLLER1 = "controller1";
    private static final String CONTROLLER2 = "controller2";
    private static final String ROUTE1      = "route1";

    @Mock
    private GeneratedItemView itemController1;

    @Mock
    private GeneratedItemView itemController2;

    @Mock
    private GeneratedItemView itemRoute1;

    @Mock
    private Project activeProject;

    @Captor
    ArgumentCaptor<BuildOptions> buildOptionsArgumentCaptor;

    @Captor
    ArgumentCaptor<List<String>> listArgumentCaptor;

    @Captor
    ArgumentCaptor<AsyncCallback> asyncCallbackCaptor;



    @Before
    public void setUp() {
        this.presenter = new YeomanPartPresenter(yeomanPartView, eventBus, foldingPanelFactory,
                                                 generatedItemViewFactory, resourceProvider, dtoFactory, builderAgent);

        // Mock folding panel factory
        doReturn(foldingPanelController).when(foldingPanelFactory).create(CONTROLLER.getLabelName());
        doReturn(CONTROLLER.getLabelName()).when(foldingPanelController).getName();
        doReturn(foldingPanelRoute).when(foldingPanelFactory).create(ROUTE.getLabelName());
        doReturn(ROUTE.getLabelName()).when(foldingPanelRoute).getName();

        // mocking items
        doReturn(itemController1).when(generatedItemViewFactory).create(CONTROLLER1, CONTROLLER);
        doReturn(itemController2).when(generatedItemViewFactory).create(CONTROLLER2, CONTROLLER);
        doReturn(itemRoute1).when(generatedItemViewFactory).create(ROUTE1, ROUTE);


        // Mocking dto factory
        doReturn(buildOptions).when(dtoFactory).createDto(BuildOptions.class);
        doReturn(buildOptions).when(buildOptions).withTargets(anyList());
        doReturn(buildOptions).when(buildOptions).withBuilderName(anyString());

        // Mocking resource provider
        doReturn(activeProject).when(resourceProvider).getActiveProject();


    }

    /**
     * Try to add an item.
     */
    @Test
    public void testAddItem() {
        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add item
        presenter.addItem(CONTROLLER1, CONTROLLER);

        // check panel has been added
        verify(yeomanPartView).addFoldingPanel(foldingPanelController);
        // item is in the list
        assertEquals(1, presenter.getWidgetByTypes().size());
        assertEquals(1, presenter.getNamesByTypes().size());

        // Check item has been added
        verify(foldingPanelController).add(itemController1);

        // name is correct
        List<String> names = presenter.getNamesByTypes().get(CONTROLLER);
        assertNotNull(names);
        assertEquals(1, names.size());
        assertEquals(CONTROLLER1, names.get(0));

        // widget is correct
        FoldingPanel foundWidget = presenter.getWidgetByTypes().get(CONTROLLER);
        assertNotNull(foundWidget);
        assertEquals(foldingPanelController, foundWidget);
        assertEquals(CONTROLLER.getLabelName(), foundWidget.getName());

    }


    /**
     * Check items are added in the same panel
     */
    @Test
    public void testAddTwoItemsSameType() {
        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);

        // check panel has been added
        verify(yeomanPartView).addFoldingPanel(foldingPanelController);
        // only one
        verify(yeomanPartView).addFoldingPanel(any(FoldingPanel.class));

        // item is in the list
        assertEquals(1, presenter.getWidgetByTypes().size());
        assertEquals(1, presenter.getNamesByTypes().size());

        // Check item has been added
        verify(foldingPanelController).add(itemController1);
        verify(foldingPanelController).add(itemController2);
        verify(foldingPanelController, times(2)).add(any(GeneratedItemView.class));


        // names are correct
        List<String> names = presenter.getNamesByTypes().get(CONTROLLER);
        assertNotNull(names);
        assertEquals(2, names.size());
        assertEquals(CONTROLLER1, names.get(0));
        assertEquals(CONTROLLER2, names.get(1));

        // widget is correct
        FoldingPanel foundWidget = presenter.getWidgetByTypes().get(CONTROLLER);
        assertNotNull(foundWidget);
        assertEquals(foldingPanelController, foundWidget);
        assertEquals(CONTROLLER.getLabelName(), foundWidget.getName());
    }


    /**
     * Check that different items are added in their own panels
     */
    @Test
    public void testAddThreeItems() {

        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);
        presenter.addItem(ROUTE1, ROUTE);

        // check panels have been added
        verify(yeomanPartView).addFoldingPanel(foldingPanelController);
        verify(yeomanPartView).addFoldingPanel(foldingPanelRoute);

        // only two times
        verify(yeomanPartView, times(2)).addFoldingPanel(any(FoldingPanel.class));

        // item is in the list
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());

        // Check item has been added
        verify(foldingPanelController).add(itemController1);
        verify(foldingPanelController).add(itemController2);
        verify(foldingPanelController, times(2)).add(any(GeneratedItemView.class));
        verify(foldingPanelRoute).add(itemRoute1);
        verify(foldingPanelRoute).add(any(GeneratedItemView.class));


        // names are correct
        // for controller
        List<String> names = presenter.getNamesByTypes().get(CONTROLLER);
        assertNotNull(names);
        assertEquals(2, names.size());
        assertEquals(CONTROLLER1, names.get(0));
        assertEquals(CONTROLLER2, names.get(1));

        // for route
        names = presenter.getNamesByTypes().get(ROUTE);
        assertNotNull(names);
        assertEquals(1, names.size());
        assertEquals(ROUTE1, names.get(0));

        // widget is correct
        FoldingPanel foundWidget = presenter.getWidgetByTypes().get(CONTROLLER);
        assertNotNull(foundWidget);
        assertEquals(foldingPanelController, foundWidget);
        assertEquals(CONTROLLER.getLabelName(), foundWidget.getName());


        foundWidget = presenter.getWidgetByTypes().get(ROUTE);
        assertNotNull(foundWidget);
        assertEquals(foldingPanelRoute, foundWidget);
        assertEquals(ROUTE.getLabelName(), foundWidget.getName());

    }


    /**
     * Try to add an existing item.
     */
    @Test
    public void testAddItemTwice() {
        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add item
        presenter.addItem(CONTROLLER1, CONTROLLER);

        // check panel has been added
        verify(yeomanPartView).addFoldingPanel(foldingPanelController);
        // item is in the list
        assertEquals(1, presenter.getWidgetByTypes().size());
        assertEquals(1, presenter.getNamesByTypes().size());

        // Check item has been added
        verify(foldingPanelController).add(itemController1);

        // name is correct
        List<String> names = presenter.getNamesByTypes().get(CONTROLLER);
        assertNotNull(names);
        assertEquals(1, names.size());
        assertEquals(CONTROLLER1, names.get(0));

        // widget is correct
        FoldingPanel foundWidget = presenter.getWidgetByTypes().get(CONTROLLER);
        assertNotNull(foundWidget);
        assertEquals(foldingPanelController, foundWidget);
        assertEquals(CONTROLLER.getLabelName(), foundWidget.getName());

        // add item
        presenter.addItem(CONTROLLER1, CONTROLLER);


    }

    /**
     * Check generate button is enabled or disabled
     */
    @Test
    public void testDisableByDefaultGenerateButton() {
        // it should have been disabled
        verify(yeomanPartView).disableGenerateButton();
        verify(yeomanPartView, times(0)).enableGenerateButton();

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);

        // it should have been called
        verify(yeomanPartView).enableGenerateButton();

    }

    /**
     * Check items can be removed
     */
    @Test
    public void testRemoveItem() {

        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);

        // item is in the list
        assertEquals(1, presenter.getWidgetByTypes().size());
        assertEquals(1, presenter.getNamesByTypes().size());

        // now delete it
        presenter.removeItem(CONTROLLER, CONTROLLER1, itemController1);

        // check all has been disabled
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // check that items has been removed
        verify(yeomanPartView).removeFoldingPanel(foldingPanelController);
        verify(yeomanPartView).removeFoldingPanel(any(FoldingPanel.class));

    }

    /**
     * Check multiple items can be removed (but category must be kept when there is only one item)
     */
    @Test
    public void testMultipleRemoveItem() {

        // check all is empty
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);
        presenter.addItem(ROUTE1, ROUTE);

        // item is in the list
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());

        // now delete it
        presenter.removeItem(CONTROLLER, CONTROLLER1, itemController1);

        // check it has been removed
        verify(foldingPanelController).remove(itemController1);

        // check no panel has been removed
        verify(yeomanPartView, times(0)).removeFoldingPanel(any(FoldingPanel.class));

        // category are still there
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());

        // now delete the other item
        presenter.removeItem(CONTROLLER, CONTROLLER2, itemController2);

        // one more item
        assertEquals(1, presenter.getWidgetByTypes().size());
        assertEquals(1, presenter.getNamesByTypes().size());

        // check that items has been removed
        verify(yeomanPartView).removeFoldingPanel(foldingPanelController);
        verify(yeomanPartView).removeFoldingPanel(any(FoldingPanel.class));

        // now delete the other item
        presenter.removeItem(ROUTE, ROUTE1, itemRoute1);

        // check all has been disabled
        assertTrue(presenter.getNamesByTypes().isEmpty());
        assertTrue(presenter.getWidgetByTypes().isEmpty());

        // check that items has been removed
        verify(yeomanPartView).removeFoldingPanel(foldingPanelRoute);
        verify(yeomanPartView, times(2)).removeFoldingPanel(any(FoldingPanel.class));

    }


    /**
     * Test generation.
     */
    @Test
    public void testGenerate() {
        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);
        presenter.addItem(ROUTE1, ROUTE);

        // it should have been called
        verify(yeomanPartView, atLeast(1)).enableGenerateButton();


        // now call generate
        presenter.generate();

        // Now check that the agent has been called one time
        verify(builderAgent).build(buildOptionsArgumentCaptor.capture(), anyString(), anyString(), anyString(), eq("yeoman"), eq(presenter));

        // check options
        verify(buildOptions).withTargets(listArgumentCaptor.capture());
        List<String> lst = listArgumentCaptor.getValue();

        // as it was a set, we may have controller or route
        int i = 0;
        if ("angular:controller".equals(lst.get(0))) {
            assertEquals("angular:controller", lst.get(i++));
            assertEquals(CONTROLLER1, lst.get(i++));
            assertEquals("angular:controller", lst.get(i++));
            assertEquals(CONTROLLER2, lst.get(i++));
            assertEquals("angular:route", lst.get(i++));
            assertEquals(ROUTE1, lst.get(i++));
        } else if ("angular:route".equals(lst.get(0))){
            assertEquals("angular:route", lst.get(i++));
            assertEquals(ROUTE1, lst.get(i++));
            assertEquals("angular:controller", lst.get(i++));
            assertEquals(CONTROLLER1, lst.get(i++));
            assertEquals("angular:controller", lst.get(i++));
            assertEquals(CONTROLLER2, lst.get(i++));
        } else {
            fail("List should contain either angular:controller or angular:route");
        }

    }

    /**
     * Check some presenter configuration
     */
    @Test
    public void checkConfig() {
        assertEquals("Yeoman", presenter.getTitle());
        assertNotNull(presenter.getTitleToolTip());
        assertNull(presenter.getTitleImage());
    }

    /**
     * Check when there is success on the finish callback
     */
    @Test
    public void checkFinishedCallbackSuccess() {
        presenter.onFinished(BuildStatus.SUCCESSFUL);

        verify(activeProject).refreshChildren(asyncCallbackCaptor.capture());

        AsyncCallback<Project> callback = asyncCallbackCaptor.getValue();
        assertNotNull(callback);

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);
        presenter.addItem(ROUTE1, ROUTE);


        // item is in the list
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());

        // check success callback
        callback.onSuccess(activeProject);

        // check that all has been removed
        assertEquals(0, presenter.getWidgetByTypes().size());
        assertEquals(0, presenter.getNamesByTypes().size());
        verify(yeomanPartView).clear();

    }


    /**
     * Check when there is failure on the finish callback
     */
    @Test
    public void checkFinishedCallbackFailure() {
        presenter.onFinished(BuildStatus.SUCCESSFUL);

        verify(activeProject).refreshChildren(asyncCallbackCaptor.capture());

        AsyncCallback<Project> callback = asyncCallbackCaptor.getValue();
        assertNotNull(callback);

        // add items
        presenter.addItem(CONTROLLER1, CONTROLLER);
        presenter.addItem(CONTROLLER2, CONTROLLER);
        presenter.addItem(ROUTE1, ROUTE);


        // item is in the list
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());

        // check failure callback
        callback.onFailure(new Exception());

        // check that nothing has been removed
        assertEquals(2, presenter.getWidgetByTypes().size());
        assertEquals(2, presenter.getNamesByTypes().size());
        verify(yeomanPartView, times(0)).clear();

    }
}
