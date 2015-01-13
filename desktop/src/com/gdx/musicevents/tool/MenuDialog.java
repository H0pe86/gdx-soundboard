package com.gdx.musicevents.tool;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gdx.musicevents.MusicEventManager;
import com.gdx.musicevents.tool.file.FileChooser;

public class MenuDialog extends Dialog {

    public MenuDialog(final Skin skin, final Stage stage,
            final MusicEventManager manager) {
        super("Menu", skin);

        Table content = getContentTable();
        content.defaults().expandX().fillX().minWidth(175);

        Button newProject = new TextButton("New", skin);
        newProject.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileChooser newProject = FileChooser.createPickDialog(
                        "Create project", skin, Gdx.files.internal("./"),
                        new FileChooser.Result() {
                            @Override
                            public boolean result(boolean success,
                                    FileHandle result) {
                                if (success) {
                                    manager.create(result);

                                    hide();
                                }
                                return true;
                            }
                        });

                newProject.setFilter(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.isDirectory();
                    }
                });
                newProject.setOkButtonText("Create");
                newProject.show(stage);

            }
        });

        content.add(newProject).row();

        final FileChooser.Result saveResult = new FileChooser.Result() {
            @Override
            public boolean result(boolean success, FileHandle result) {

                if (success) {
                    if (result.isDirectory() || result.path().contains(".json")) {
                        return false;
                    }

                    manager.save(result);
                    hide();
                }
                return true;
            }
        };

        /* Save button. */
        Button save = new TextButton("Save", skin);
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileChooser save = FileChooser.createSaveDialog("Save project",
                        skin, Gdx.files.internal("./"), saveResult);
                save.show(stage);
            }
        });
        content.add(save).row();

        /* Load button. */
        Button load = new TextButton("Load", skin);
        load.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileChooser load = FileChooser.createLoadDialog("Load project",
                        skin, Gdx.files.internal("./"),
                        new FileChooser.Result() {

                            @Override
                            public boolean result(boolean success,
                                    FileHandle result) {
                                if (success) {
                                    manager.load(result);
                                    hide();
                                }
                                return true;
                            }
                        });
                load.show(stage);
            }
        });
        content.add(load).row();

        /* Exit button. */
        Button exit = new TextButton("Exit", skin);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        content.add(exit).row();

        /* Cancel button. */
        Button cancel = new TextButton("Cancel", skin);
        cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        content.add(cancel).row();

        key(Keys.ESCAPE, true);
    }

}
