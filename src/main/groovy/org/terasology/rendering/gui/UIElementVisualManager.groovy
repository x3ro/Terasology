package org.terasology.rendering.gui



class UIElementVisualManager {

    def element
    def states = [:]

    UIElementVisualManager(config) {
        println(config)
        //this.element = createUIElement(config)
        //this.element.visualManager = this;
    }

    def createUIElement(config) {
        // Get the UIElement subclass name as specified in the "class" field.
        // Obviously needs some error checking, too.
        def clazz = Class.forName(config["class"])

        // Create a new instance of the retrieved UIElement subclass
        this.element = clazz.newInstance(config["id"])

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
