package org.terasology.rendering.gui;


import org.terasology.rendering.gui.widgets.UIWindow;

public abstract class UIController {
    public UIWindow window;

    public void setWindow(UIWindow w) {
        window = w;
        w.setController(this);
        registerEventHandlers();
    }

    /**
     * Invoked after the window and all its widgets have been created, this method should be
     * used to add event handlers for the widgets if necessary. To access the individual widgets,
     * UIWindow#getChild with the widget id should be used.
     */
    abstract public void registerEventHandlers();
}
