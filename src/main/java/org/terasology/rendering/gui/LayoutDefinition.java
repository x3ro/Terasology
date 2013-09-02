/*
 * Copyright 2013 MovingBlocks
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
package org.terasology.rendering.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.asset.AssetData;
import org.terasology.rendering.gui.framework.UIDisplayContainer;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.widgets.UIWindow;

import java.util.List;
import java.util.Map;

public class LayoutDefinition implements AssetData {

    private static final Logger logger = LoggerFactory.getLogger(LayoutDefinition.class);

    public String id;
    public String controller;
    public String component;

    // The outer elements are the element's states, the inner elements are style
    // attribute for a state. For example,
    //      style.get("hover").get("position")
    // Would get the "position" style attribute in the "hover" state.
    public Map<String, Map<String, Object>> style;
    public List<LayoutDefinition> children;

    public final String defaultWidgetPackage = "org.terasology.rendering.gui.widgets";
    public final String defaultControllerPackage = "org.terasology.rendering.gui.controllers";



    /**
     * @return A new window based on the current layout definition.
     */
    public UIWindow createWindow() {
        UIWindow window = new UIWindow();
        window.setModal(true);
        window.maximize();
        applyTo(window);

        if (controller != null) {
            UIController c = instantiateController(this.controller);
            c.setWindow(window);
        }

        return window;
    }

    /**
     * Applies the styles represented by the LayoutDefinition to the given window.
     * @throws IllegalArgumentException if the LayoutDefinition is meant for a widget, that is, it has the
     *         "component" attribute set.
     */
    public void applyTo(UIWindow window) {
        if(component != null) {
            throw new IllegalArgumentException("Tried to create a window from a layout definition containing a component, which indicates that it defines a widget.");
        }

        // Apply window styles
        if(style != null) {
            for(Map.Entry<String, Object> e : style.get("default").entrySet()) {
                StyleApplicator.applyStyle(window, e.getKey(), e.getValue());
            }
        }

        // Create children widgets
        if(children != null) {
            for(LayoutDefinition child : children) {
                UIDisplayElement el = child.createWidget();
                logger.debug("Adding display element '{}' to window with id '{}'", el.getId(), id);
                window.addDisplayElement(el);
            }
        }

        window.setId(id);
    }

    public UIDisplayContainer createWidget() {
        UIDisplayContainer widget = instantiateComponent();
        if(widget == null) {
            throw new IllegalArgumentException("Could not create widget with id " + id);
        }

        for(Map.Entry<String, Object> e : style.get("default").entrySet()) {
            StyleApplicator.applyStyle(widget, e.getKey(), e.getValue());
        }

        widget.setId(id);
        widget.setVisible(true);
        return widget;
    }

    /**
     * @todo this method and instantiateComponent must be properly refactored!
     */
    private UIController instantiateController(String c) {
        String controllerName = c;

        // If the component is not given including package(s), assume that it
        // lives in the default widget package.
        if (!controllerName.contains(".")) {
            controllerName = defaultControllerPackage + "." + controllerName;
        }

        Class controllerClass;
        try {
            controllerClass = Class.forName(controllerName);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find class matching controller '{}'", controllerName, e);
            return null;
        }

        UIController instance;
        try {
            instance = (UIController) controllerClass.newInstance();
        } catch (ClassCastException | IllegalAccessException | InstantiationException e) {
            logger.error("Could not instantiate class matching controller '{}'", controllerName, e);
            return null;
        }

        return instance;
    }

    private UIDisplayContainer instantiateComponent() {
        // If the component is not given including package(s), assume that it
        // lives in the default widget package.
        if(!component.contains(".")) {
            component = defaultWidgetPackage + "." + component;
        }

        Class widgetClass;
        try {
            widgetClass = Class.forName(component);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find class matching component '{}'", component, e);
            return null;
        }

        UIDisplayContainer instance;
        try {
            instance = (UIDisplayContainer) widgetClass.newInstance();
        } catch (ClassCastException | IllegalAccessException | InstantiationException e) {
            logger.error("Could not intantiate class matching component '{}'", component, e);
            return null;
        }

        return instance;
    }
}
