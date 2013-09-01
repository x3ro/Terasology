package org.terasology.rendering.gui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.rendering.gui.UIController;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ClickListener;
import org.terasology.rendering.gui.widgets.UIButton;
import org.terasology.rendering.gui.windows.UIMenuSelectWorld;


public class MainMenuController extends UIController {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuController.class);

    @Override
    public void registerEventHandlers() {

        window.getChild(UIButton.class, "singleplayer-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("test");
            }
        });
    }
}
