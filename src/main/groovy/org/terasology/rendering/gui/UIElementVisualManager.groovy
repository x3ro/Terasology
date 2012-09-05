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
        def clazz = Class.forName("org.terasology.rendering.gui.widgets.${config["component"]}")

        // Create a new instance of the retrieved UIElement subclass
        this.element = clazz.newInstance()
        this.element.id = config["id"]

        this.states["_default"] = config["style"]
        this.element.visualStates.each() { states[it] = config["style:${it}"] }

        // Apply all parameters configured via JSON for the default state
        applyState("_default")

        return this.element
    }


    /**
     * Activates the given state (and the associated styles) for the
     * managed by this UIElementVisualManager
     * @param name Name of the state to be activated
     */
    def void applyState(String name) {
        applyState(this.states[name], element)
    }


    /**
     * Applies the given Map of styles to the element by setting the property with
     * the respective name. When setting properties on a Java object, Groovy invokes a
     * setter with named "set<PropertyName>", e.g. if the property was named "background",
     * the setter invoked would be "setBackground".
     * @param styles
     * @param element
     */
    static def void applyState(Map styles, UIDisplayElement element) {
        styles.each() { key, value -> element."${key}" = value }
    }
}
