package com.gdx.musicevents;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class MusicEventManager {

    private final ObjectMap<String, MusicEvent> events = new ObjectMap<String, MusicEvent>();

    private MusicEvent currentEvent;

    private final Array<MusicEventListener> listeners = new Array<MusicEventListener>();


    private final Array<Effect> transitions = new Array<Effect>();


    public void play(String eventName){
        MusicEvent nextEvent = events.get(eventName);
        if(nextEvent != null){

            if(currentEvent != nextEvent) {
                MusicEvent oldEvent = currentEvent;
                currentEvent = nextEvent;

                handleTransition(currentEvent, oldEvent);
            } else {
                currentEvent.getMusic().play();
            }
        }
    }


    public void handleTransition(MusicEvent currentEvent, MusicEvent oldEvent){
        transitions.add(currentEvent.startTransition(oldEvent));
        if(oldEvent != null) {
            transitions.add(oldEvent.endTransition(currentEvent));
        }
    }

    public void play(Enum<?> eventName){
        play(eventName.name());
    }


    public void update(float dt){

        for(int i = transitions.size - 1; i >= 0 ; i--){
            Effect effect = transitions.get(i);
            effect.update(dt);

            if(effect.isDone()){
                transitions.removeIndex(i);
            }
        }

        if(currentEvent != null){
            currentEvent.update(dt);
        }
    }

    public void add(MusicEvent event){
        this.events.put(event.getName(), event);
        for(int i = 0; i < listeners.size; i++){
            MusicEventListener observer = listeners.get(i);
            observer.eventAdded(event);
        }

    }

    public void remove(MusicEvent event){
        remove(event.getName());
    }
    public void remove(String eventName){
        MusicEvent event = this.events.remove(eventName);
        if(event != null) {
            for (int i = 0; i < listeners.size; i++) {
                MusicEventListener observer = listeners.get(i);
                observer.eventRemoved(event);
            }
            event.dispose();

            if(currentEvent == event){
                currentEvent = null;
            }
        }


    }

    public void save(String fileName){


    }

    public void load(String fileName){

    }

    public MusicEvent getCurrentEvent() {
        return currentEvent;
    }

    public void stop() {
        if(this.currentEvent != null){
            currentEvent.getMusic().stop();
        }
    }

    public void clear() {
        for(MusicEvent event : events.values()){
            event.dispose();
            for (int i = 0; i < listeners.size; i++) {
                MusicEventListener observer = listeners.get(i);
                observer.eventRemoved(event);
            }

        }
        events.clear();
    }

    public void addListener(MusicEventListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(MusicEventListener listener){
        this.listeners.removeValue(listener, true);
    }


    public Array<MusicEvent> getEvents(){
        return events.values().toArray();
    }
}