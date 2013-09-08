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


import org.terasology.rendering.gui.widgets.UIWindow;

public abstract class UIController {
    public UIWindow window;

    public void setWindow(UIWindow w) {
        window = w;
        w.setController(this);
        initialize();
    }

    /**
     * Invoked after the window and all its widgets have been created, this method should be
     * used to add event handlers for the widgets if necessary, or to perform other initialization.
     * To access the individual widgets, UIWindow#getChild with the widget id should be used.
     */
    abstract public void initialize();
}
