package org.terasology.testUtil;

import org.terasology.asset.AssetManager;
import org.terasology.asset.AssetType;
import org.terasology.asset.sources.ClasspathSource;
import org.terasology.engine.CoreRegistry;
import org.terasology.engine.module.ModuleManager;
import org.terasology.utilities.collection.Tuple2;

import java.net.MalformedURLException;
import java.net.URL;

public class AssetHelper {

    /**
     * Adds an asset source "unittest" to the asset manager, making it possible to put test assets in
     * "test/resource/assets/" if they serve only for unit testing.
     * @param testClass The class instance where the method is executed (needed to locate the resource path).
     * @return A tuple containing the created Module- and AssetManager
     * @throws MalformedURLException
     */
    public static Tuple2<ModuleManager, AssetManager> addTestAssetPath(Object testClass) throws MalformedURLException {
        ModuleManager moduleManager = new ModuleManager();
        moduleManager.applyActiveModules();
        AssetManager assetManager = new AssetManager(moduleManager);
        CoreRegistry.put(ModuleManager.class, moduleManager);
        CoreRegistry.put(AssetManager.class, assetManager);
        AssetType.registerAssetTypes(assetManager);
        URL url = testClass.getClass().getClassLoader().getResource("testResources");
        url = new URL(url.toString().substring(0, url.toString().length() - "testResources".length() - 1));
        assetManager.addAssetSource(new ClasspathSource("unittest", url, ModuleManager.ASSETS_SUBDIRECTORY, ModuleManager.OVERRIDES_SUBDIRECTORY));

        return new Tuple2<ModuleManager, AssetManager>(moduleManager, assetManager);
    }
}
