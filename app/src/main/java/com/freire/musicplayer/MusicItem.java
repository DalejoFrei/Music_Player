package com.freire.musicplayer;

import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.LinkedList;

public class MusicItem {
    public int id;
    private String title;
    private boolean isFavorite;
    private long duration;
    private String link;
    private ImageView trackImg;
    File path;
    private boolean queued = false;
    public boolean isPaused; //used to track the play state of songs
    private LinkedList<MusicItem>queuedSongs;

    public MusicItem(int id, String title){
        this.title = title;
        this.id = id;
        this.duration = 500;
        queuedSongs = new LinkedList<>();
    }
    public MusicItem(int id, MusicItem original){
        this.id = id;
        this.title = original.getTitle();
        this.duration = original.duration;
        /*
        this.link = original.link;
        this.trackImg = original.getTrackImg();
        if(original.path == null){
            Log.e("new Music path", "new music item path not set");
        }else {
            this.path = original.path;
        }

         */
    }
    public MusicItem(int id, String title, String link){
        this.title = title;
        this.id = id;
        this.link = link;
    }
    public String getTitle(){
        return this.title;
    }
    public int getId(){
        return this.id;
    }
    public long getDuration(){return this.duration;}
    public void setTrackImg(ImageView view){this.trackImg = view;}
    public ImageView getTrackImg(){return this.trackImg;}

    public boolean isQueued(){return this.queued;}
    public LinkedList<MusicItem> getQueue(){
        return queuedSongs;
    }
    public boolean hasQueue(){
        if(queuedSongs == null || queuedSongs.size() == 0) {
            Log.e("hasQueue", title + " does not have a queue");
            return false;
        }
        Log.e("hasQueue", title + " has a queue");
        return true;
    }
    public LinkedList<MusicItem> addToQueue(MusicItem add){
        if(queuedSongs == null)
            queuedSongs = new LinkedList<>();
        queuedSongs.add(add);
        return queuedSongs;
    }
    public LinkedList<MusicItem> pop(){
        queuedSongs.removeFirst();
        if(queuedSongs.size() == 0)
            killQueue();
        return queuedSongs;
    }
    public void killQueue(){
        queuedSongs = null;
    }
}
