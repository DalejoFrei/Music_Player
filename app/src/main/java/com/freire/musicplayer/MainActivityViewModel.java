package com.freire.musicplayer;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivityViewModel extends ViewModel{
    public int selectedSongId;
    private MutableLiveData<MusicItem> currentSong = new MutableLiveData<>();
    private MediaPlayer music;
    private MutableLiveData<ArrayList<MusicItem>> musicItems = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Playlist>> playlists = new MutableLiveData<>();
    private Playlist selectedPlaylist;
    private MutableLiveData<LinkedList<MusicItem>> queue = null;
    private MusicItem tmp = null;

    private Repository myRepo;

    public MainActivityViewModel(){
        myRepo = new Repository();
        selectedSongId = 0;
        Log.e("created", "viewModel");
        musicItems.setValue(myRepo.getMusicItems());
        playlists.setValue(myRepo.getPlaylists());
        selectedPlaylist = new Playlist("null");
    }
    public void initializeQueue(){
        if(queue == null){
            queue = new MutableLiveData<>();
        }else{
            Log.e("prepopQueue", "queue not empty");
        }
    }
    public void showQueue(){
        for(int i = 0; i < this.queue.getValue().size(); i++){
            Log.e("track queue", "Queue item: " + this.queue.getValue().get(i).getTitle());
        }
    }
    /*
    **Getters
     */
    public int getSelectedSongId(){
        return this.selectedSongId;
    }
    public MutableLiveData<ArrayList<MusicItem>> getMusicItems(){return this.musicItems;}
    public ArrayList<Playlist> getPlaylists(){return this.playlists.getValue();}
    public MutableLiveData<ArrayList<Playlist>> getPlaylistMutableLiveData(){return playlists;}
    public String[] getHomeMenuItems(){return myRepo.getHomeMenuItems();}
    public MusicItem getCurrentSong(){
        return this.currentSong.getValue();
    }
    public MutableLiveData<MusicItem> getCurrentSongMLD(){return this.currentSong;}
    public MutableLiveData<LinkedList<MusicItem>> getQueue(){
        initializeQueue();
        return this.queue;
    }

    public Playlist getPlaylist(Playlist playlist){
        Playlist result = new Playlist("");
        for(Playlist i : getPlaylists()){
            if(i.name == playlist.name){
                result.name = i.name;
            }
        }
        return result;
    }
    public Playlist getSelectedPlaylist(){
        if(this.selectedPlaylist == null){
            Log.e("selectedPlaylist", "No playlist selected");
            return null;
        }else{
            Log.e("selectedPlaylist", "Playlist: " + selectedPlaylist.name);
        }
        return this.selectedPlaylist;
    }

    /*
     ** Setters
     */
    public void setPlaylists(ArrayList<Playlist> input){this.playlists.setValue(input);}
    public void setSelectedPlaylist(Playlist playlist){
        this.selectedPlaylist = playlist;
    }
    public void setMusic(MediaPlayer music){
        this.music = music;
    }
    public MusicItem setCurrentSong(MusicItem set) {
            this.currentSong.setValue(set);
            selectedSongId = this.currentSong.getValue().getId();
            Log.e("current", "Current song: " + this.currentSong.getValue().getTitle());

        return this.currentSong.getValue();
    }


    /*
     ** Playlist editors
     */

    public ArrayList<MusicItem> removeSong(MusicItem ditch){
        musicItems.getValue().remove(ditch);
        return musicItems.getValue();
    }
    public MusicItem removeFromPlaylist(MusicItem ditch){
        getSelectedPlaylist().removeTrack(ditch);
        return ditch;
    }
    public Playlist removePlaylist(Playlist ditch){
        ArrayList<Playlist> result = getPlaylists();
        result.remove(ditch);
        this.playlists.setValue(result);
        return ditch;
    }
    public void addSongToPlaylist(Playlist target, MusicItem newSong){
        for(int i = 0; i < getPlaylists().size(); i++){
            if(target.getName() == playlists.getValue().get(i).name){
                Log.e("added to playlist", "added to playlist " + playlists.getValue().get(i).getName());
                playlists.getValue().get(i).addTrack(newSong);
            }
        }
    }
    public Playlist addPlaylist(Playlist add){
        //add a playlist to the list

        this.playlists.getValue().add(add);
        return add;
    }

    public void toAllMI(){
        this.musicItems.setValue(myRepo.getMusicItems());
    }
}
