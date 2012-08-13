package org.terasology.asset.loaders;

import org.terasology.asset.AssetLoader;
import org.terasology.asset.AssetUri;
import org.terasology.rendering.gui.UIElementUnserializer;
import org.terasology.rendering.gui.framework.UIDisplayWindow;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lucas
 * Date: 8/12/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuDefinitionLoader implements AssetLoader<UIDisplayWindow> {
    @Override
    public UIDisplayWindow load(InputStream stream, AssetUri uri, List<URL> urls) throws IOException {
        Reader r = new InputStreamReader(stream);
        UIElementUnserializer.unserialize(r);
        System.out.println("yay");
        System.exit(0);
        return null;
    }
}
