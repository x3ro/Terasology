package org.terasology.rendering.gui

import groovy.json.*;

class UIElementUnserializer {

    static def unserialize(java.io.Reader reader) {
        // Read and parse the UIElement configuration
        def data = new JsonSlurper().parse(reader)
        println(data)
        // One could do some structure checking here, e.g. if the JSON conforms to what we expect.

        // We are ready to create the actual UIElement instances (Java classes)
        //def elements = data.collect() { new UIElementVisualManager(it).element }

        //return elements
    }

}
