/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.gui.controllers;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.config.Config;
import org.terasology.engine.CoreRegistry;
import org.terasology.engine.GameEngine;
import org.terasology.engine.modes.StateLoading;
import org.terasology.engine.paths.PathManager;
import org.terasology.game.GameManifest;
import org.terasology.network.NetworkMode;
import org.terasology.rendering.gui.UIController;
import org.terasology.rendering.gui.dialogs.UIDialogCreateNewWorld;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ClickListener;
import org.terasology.rendering.gui.widgets.UIButton;
import org.terasology.rendering.gui.widgets.UIList;
import org.terasology.rendering.gui.widgets.UIListItem;
import org.terasology.utilities.FilesUtil;

import javax.vecmath.Vector4f;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

public class SelectWorldController extends UIController {
    private static final Logger logger = LoggerFactory.getLogger(SelectWorldController.class);

    private UIList list;

    /**
     * Whether or not selecting a game in the world selector will launch
     * a multi-player (i.e. server) game.
     */
    private boolean createServerGame = false;

    @Override
    public void initialize() {

        list = window.getChild(UIList.class, "world-selector");
        list.addDoubleClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                loadSelectedWorld();
            }
        });

        window.getChild(UIButton.class, "back-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                window.getGUIManager().openWindow("main");
            }
        });

        window.getChild(UIButton.class, "load-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                loadSelectedWorld();
            }
        });

        window.getChild(UIButton.class, "delete-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                deleteSelectedWorld();
            }
        });

        window.getChild(UIButton.class, "create-button").addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                UIDialogCreateNewWorld dialog = new UIDialogCreateNewWorld(createServerGame);
                dialog.open();
            }
        });


        fillList();
    }

    private void loadSelectedWorld() {

        if (list.getItemCount() < 1) {
            window.getGUIManager().showMessage("Error", "You did not create a world yet!");
            return;
        }

        if (list.getSelection() == null) {
            window.getGUIManager().showMessage("Error", "Please choose a world!");
            return;
        }

        try {
            GameManifest info = (GameManifest) list.getSelection().getValue();
            Config config = CoreRegistry.get(Config.class);

            config.getWorldGeneration().setDefaultSeed(info.getSeed());
            config.getWorldGeneration().setWorldTitle(info.getTitle());
            CoreRegistry.get(GameEngine.class).changeState(new StateLoading(info, (createServerGame) ? NetworkMode.SERVER : NetworkMode.NONE));
        } catch (Exception e) {
            window.getGUIManager().showMessage("Error", "Failed reading saved game. Sorry.");
        }
    }

    public void deleteSelectedWorld() {
        if (list.getSelection() == null) {
            window.getGUIManager().showMessage("Error", "Please choose a saved game first.");
            return;
        }

        try {
            GameManifest gameManifest = (GameManifest) list.getSelection().getValue();
            Path world = PathManager.getInstance().getSavePath(gameManifest.getTitle());
            FilesUtil.recursiveDelete(world);
            list.removeItem(list.getSelectionIndex());
        } catch (Exception e) {
            logger.error("Failed to delete saved game", e);
            window.getGUIManager().showMessage("Error", "Failed to remove saved game - sorry.");
        }
    }

    public void fillList() {
        list.removeAll();

        Path savedGames = PathManager.getInstance().getSavesPath();
        SortedMap<FileTime, Path> savedGamePaths = Maps.newTreeMap(Collections.reverseOrder());
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(savedGames)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry.resolve(GameManifest.DEFAULT_FILE_NAME))) {

                    savedGamePaths.put(Files.getLastModifiedTime(entry), entry);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read saved games path", e);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map.Entry<FileTime, Path> world : savedGamePaths.entrySet()) {
            Path gameManifest = world.getValue().resolve(GameManifest.DEFAULT_FILE_NAME);

            if (!Files.isRegularFile(gameManifest)) {
                continue;
            }
            try {
                GameManifest info = GameManifest.load(gameManifest);
                if (!info.getTitle().isEmpty()) {
                    Date date = new Date(world.getKey().toMillis());
                    UIListItem item = new UIListItem(info.getTitle() + "\n" + dateFormat.format(date), info);
                    item.setPadding(new Vector4f(10f, 5f, 10f, 5f));
                    list.addItem(item);
                }
            } catch (IOException e) {
                logger.error("Failed reading world data object.", e);
            }
        }
    }

    public int getWorldCount() {
        return list.getItemCount();
    }

    public void setCreateServerGame(boolean createServerGame) {
        this.createServerGame = createServerGame;
    }
}
