package org.terasology.rendering.gui;

import org.junit.Before;
import org.junit.Test;
import org.terasology.asset.AssetManager;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.engine.module.ModuleManager;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.rendering.gui.widgets.UIWindow;
import org.terasology.testUtil.AssetHelper;
import org.terasology.utilities.collection.Tuple2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LayoutDefinitionTest {

    AssetManager assetManager;

    @Before
    public void setup() throws Exception {
        Tuple2<ModuleManager, AssetManager> t = AssetHelper.addTestAssetPath(this);
        ModuleManager moduleManager = t._1();
        assetManager = t._2();
    }

    @Test
    public void loadAssetData() {
        LayoutDefinition def = assetManager.loadAssetData(new AssetUri("layout:unittest:simple"), LayoutDefinition.class);
        assertNotNull(def);
    }




}
