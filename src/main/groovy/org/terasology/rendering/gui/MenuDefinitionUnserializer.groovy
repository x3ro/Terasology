package org.terasology.rendering.gui

import groovy.json.*
import org.terasology.rendering.gui.widgets.UIWindow
import org.terasology.asset.AssetUri;


/*+
 * Creates a fully configured UIDisplayWindow instance from a JSON menu definition.
 * @author Lucas Jenss <public@x3ro.de>
 */
class MenuDefinitionUnserializer {

    static def unserialize(java.io.Reader reader, AssetUri assetUri) {
        def data = new JsonSlurper().parse(reader)

        // TODO: One could do some structure checking here, e.g. if the JSON conforms
        // to what we expect.

        // All the actual work is left to the UIElementVisualManager, which instantiates
        // the UIDisplayElement subclasses and manages their styles.
        def elements = data["children"].collect() { new UIElementVisualManager(it).element }

        // Now the UIWindow with all the generated elements is created, and the base styles
        // for the window are applied (such as background)
        def x = new UIWindow(data["id"], assetUri, elements)
        UIElementVisualManager.applyState(data["style"], x)

        return x
    }

}
