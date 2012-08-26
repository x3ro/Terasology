package org.terasology.rendering.gui

import org.terasology.rendering.gui.framework.UIDisplayElement



class UIElementVisualManager {

    UIDisplayElement element
    def states = [:]

    UIElementVisualManager(config) {
        this.element = createUIElement(config)
        this.element.visualManager = this;
    }

    def createUIElement(config) {
        // Get the UIElement subclass name as specified in the "class" field.
        // Obviously needs some error checking, too.
        def clazz = Class.forName("org.terasology.rendering.gui.components.${config["component"]}")

        // Create a new instance of the retrieved UIElement subclass
        this.element = clazz.newInstance()
        this.element.id = config["id"]

        this.states["_default"] = config["style"]
        this.element.visualStates.each() { states[it] = config["style:${it}"] }

        // Apply all parameters configured via JSON for the default state
        applyState("_default")

        return this.element
    }

    def applyState(name) {
        this.states[name].each() { key, value -> element."${key}" = value }
    }


}
