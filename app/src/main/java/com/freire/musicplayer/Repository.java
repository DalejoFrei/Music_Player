package com.freire.musicplayer;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repository {
    private ArrayList<MusicItem> musicItems = new ArrayList<MusicItem>();
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private static final String[] homeMenuItems = {"Playlists", "Tracks", "Artists", "Genres"};
    public Repository(){
        populate();
    }
    /*
    GETTERS
     */
    public ArrayList<MusicItem> getMusicItems(){return this.musicItems;}
    public ArrayList<Playlist> getPlaylists() {return playlists;}
    public String[] getHomeMenuItems(){return homeMenuItems;}

    //NEEDS TO PARSE FILES
    public void populate(){
        MusicItem hold = null;
        MusicItem next = null;
        //populates a TEST array of music items
        for(int i = 0; i < 10; i++){
            MusicItem current = new MusicItem(i, ""+ i + " song");
            current.id = i;
            musicItems.add(current);
            Log.e("added song", "" + current.getTitle());
        }

        //populates a TEST array of Playlists
        for(int i = 0; i < 5; i++){
            Playlist temp = new Playlist("Playlist " + i);
            temp.addTrack(this.musicItems.get(i));

            playlists.add(temp);
            Log.e("added playlist", "" + temp.getName());
        }
        playlists.add(new Playlist("Artists"));
        playlists.add(new Playlist("Genres"));
        //musicItems = parseList(musicItems);
    }
    /*
    public ArrayList<MusicItem> parseList(ArrayList<MusicItem> list){ //ads pointers to music items list
        ArrayList<MusicItem> result = new ArrayList<MusicItem>();
        MusicItem temp = list.get(0);
        MusicItem prev = null;
        MusicItem next = null;
        result.add(temp);
        //parses and finalizes list of musicItems
        for(int i = 1; i < list.size(); i++){
            temp = list.get(i);
            prev = list.get(i - 1);
            temp.setPreviousSong(prev);
            temp.getPreviousSong().setNextSong(temp);
            if(i < list.size()-1) {
                next = list.get(i + 1);
                temp.setNextSong(next);//set next song
                result.add(temp);
                Log.e("Temp", "|" + "prev: " + temp.getPreviousSong().getTitle() + "|" + "tmp: " + temp.getTitle() + "|" + "next: " + temp.getNextSong().getTitle() + "|");
            }else{
                temp.setNextSong(list.get(0));
                result.get(0).setPreviousSong(temp); //link head to tail
                result.add(temp);
                Log.e("list.size()", "|" + "prev: " + temp.getPreviousSong().getTitle() + "|" + "tmp: " + temp.getTitle() + "|" + "next: " + temp.getNextSong().getTitle() + "|");
            }
        }
        return result;
    }

     */
}
