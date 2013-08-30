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

import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.asset.AssetData;
import org.terasology.rendering.gui.framework.UIDisplayContainer;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.widgets.UILabel;
import org.terasology.rendering.gui.widgets.UIWindow;

import javax.vecmath.Vector2f;
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



    /**
     * @return A new window based on the current layout definition.
     */
    public UIWindow createWindow() {
        if(component != null) {
            throw new IllegalArgumentException("Tried to create a window from a layout definition containing a component, which indicates that it defines a widget.");
        }

        UIWindow window = new UIWindow();

        // Apply window styles
        for(Map.Entry<String, Object> e : style.get("default").entrySet()) {
            StyleApplicator.applyStyle(window, e.getKey(), e.getValue());
        }

        // Create children widgets
        for(LayoutDefinition child : children) {
            UIDisplayElement el = child.createWidget();
            logger.debug("Adding display element '{}' to window with id '{}'", el.getId(), id);
            window.addDisplayElement(el);
        }

        window.setId(id);
        window.setModal(true);
        window.maximize();

        return window;
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

    private UIDisplayContainer instantiateComponent() {
        // If the component is not given including package(s), assume that it
        // lives in the default widget package.
        if(!component.contains(".")) {
            component = defaultWidgetPackage + "." + component;
        }

        Class widgetClass = null;
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
