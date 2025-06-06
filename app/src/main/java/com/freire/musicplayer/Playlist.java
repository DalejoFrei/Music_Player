package com.freire.musicplayer;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Playlist {
    private String name;
    public MutableLiveData<ArrayList<MusicItem>> tracks;
    public Playlist(String name){
        this.name = name;
        tracks = new MutableLiveData<>();
        tracks.setValue(new ArrayList<MusicItem>());
    }
    public ArrayList<MusicItem> getTracks(){
        return this.tracks.getValue();
    }

    public String getName() {
        return name;
    }

    //Auto generates song IDs
    private int generateTrackID(){
        int forgedID = (int)(Math.random()*1000) + this.tracks.getValue().size();
        //check for duplicates.. can't have multiple songs have the same id
        for(MusicItem piece : this.tracks.getValue()){
            if(piece.getId() == forgedID)
                generateTrackID();
        }
        return forgedID;
    }

    public void addTrack(MusicItem track){
        int ID = generateTrackID();
        MusicItem tmp = new MusicItem(ID, track);
        this.tracks.getValue().add(tmp);
    }
    public void removeTrack(MusicItem track){
        this.tracks.getValue().remove(track);
        Log.e("removeTrack", "Removed " + track.getTitle());
        this.tracks.setValue(this.tracks.getValue());
    }
    public void rename(String name){this.name = name;}
    public String printTracks(){
        String result = "Tracks: ";
        for(int i = 0; i < this.tracks.getValue().size(); i++){
            result += this.tracks.getValue().get(i).getTitle() + ", ";
        }
        return result;
    }
}
