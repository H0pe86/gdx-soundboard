package com.gdx.musicevents.tool;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.gdx.musicevents.MusicEventManager;
import com.gdx.musicevents.State;
import com.gdx.musicevents.Track;

public class StateInfoPanel extends Table {
    private final Label nowPlaying;
    
    private State state;
    public StateInfoPanel(final Skin skin, final Stage stage, final MusicEventManager manager) {
        super(skin);

        this.left().pad(2);
        nowPlaying = new Label("", skin);
        this.add(nowPlaying).left();
        this.add().expandX().fillX();

        final Button play = new TextButton("Play", skin);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.play();
            }
        });
        this.add(play);
        
        final Button stop = new TextButton("Stop", skin);
        stop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.stop();
            }
        });
        this.add(stop);
        
        final Button menu = new TextButton("Menu", skin);
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(menu.isChecked()){
                    MenuDialog menuDialog = new MenuDialog(skin, manager);
                    menuDialog.show(stage);
                    menu.setChecked(false);
                }
            }
        });
        
        this.add(menu).right();
    }
    
    public void show(State state){
        this.state = state;
        if(state == null || !state.isPlaying()){
            nowPlaying.setText("Paused");
        }
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        
        if(state != null && state.isPlaying()){

            Track track = state.getCurrentTrack();
            int intPosition = (int)(track.getPosition() * 100);
            String twoDecimalForm = Float.toString(intPosition / 100.0f);

            nowPlaying.setText("Now playing: " + state.toString() + " " + twoDecimalForm);
        } else {
            nowPlaying.setText("Paused");
        }
        
    }
}