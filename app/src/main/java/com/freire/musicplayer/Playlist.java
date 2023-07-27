package com.freire.musicplayer;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Playlist {
    String name;
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

    public void addTrack(MusicItem track){this.tracks.getValue().add(track);}
    public void removeTrack(MusicItem track){
        this.tracks.getValue().remove(track);
        Log.e("removeTrack", "Removed " + track.getTitle());
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
