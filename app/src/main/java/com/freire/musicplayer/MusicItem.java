package com.freire.musicplayer;

import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class MusicItem {
    public int id;
    private String title;
    private int duration;
    private String link;
    private MusicItem nextSong;
    private MusicItem previousSong;
    private ImageView trackImg;
    File musicFile;

    public MusicItem(int id, String title){
        this.title = title;
        this.id = id;
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
    public void setTrackImg(ImageView view){this.trackImg = view;}
    public ImageView getTrackImg(){return this.trackImg;}

    public void setNextSong(MusicItem next){this.nextSong = next;}
    public MusicItem getNextSong(){return this.nextSong;}
    public MusicItem getPreviousSong(){return this.previousSong;}
    public void setPreviousSong(MusicItem previous){this.previousSong = previous;}

}
