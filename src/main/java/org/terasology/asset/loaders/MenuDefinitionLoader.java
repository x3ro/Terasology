package org.terasology.asset.loaders;

import org.terasology.asset.AssetLoader;
import org.terasology.asset.AssetUri;
import org.terasology.rendering.gui.MenuDefinitionUnserializer;
import org.terasology.rendering.gui.framework.UIDisplayWindow;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

/**
 * Invokes the menu definition unserializer whenever a JSON menu definition
 * shall be loaded.
 * @author Lucas Jenss <public@x3ro.de>
 */
public class MenuDefinitionLoader implements AssetLoader<UIDisplayWindow> {
    @Override
    public UIDisplayWindow load(InputStream stream, AssetUri uri, List<URL> urls) throws IOException {
        Reader r = new InputStreamReader(stream);
        return (UIDisplayWindow) MenuDefinitionUnserializer.unserialize(r, uri);
    }
}
