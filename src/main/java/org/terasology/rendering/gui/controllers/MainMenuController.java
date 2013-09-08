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
package org.terasology.rendering.gui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.CoreRegistry;
import org.terasology.engine.GameEngine;
import org.terasology.rendering.gui.UIController;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ClickListener;
import org.terasology.rendering.gui.widgets.UIButton;
import org.terasology.rendering.gui.windows.UIMenuSelectWorld;


public class MainMenuController extends UIController {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuController.class);

    @Override
    public void initialize() {
        window.getChild(UIButton.class, "singleplayer-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("selectworld");
                ((UIMenuSelectWorld) window.getGUIManager().getWindowById("selectworld")).setCreateServerGame(false);
            }
        });

        window.getChild(UIButton.class, "hostgame-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("selectworld");
                ((UIMenuSelectWorld) window.getGUIManager().getWindowById("selectworld")).setCreateServerGame(true);
            }
        });

        window.getChild(UIButton.class, "joingame-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("joinserver");
            }
        });

        window.getChild(UIButton.class, "settings-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("config");
            }
        });

        window.getChild(UIButton.class, "exit-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                CoreRegistry.get(GameEngine.class).shutdown();
            }
        });
    }
}
