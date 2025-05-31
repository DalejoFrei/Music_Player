package com.freire.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    //private LinkedList<MusicItem> queue = null;
//    private MusicQueue queue = new MusicQueue();


    private MusicItem tmp = null;
    private ArrayList<MusicItem> selectedList = musicItems.getValue();//to be set to whatever the user left off on during their last session.

    private Repository myRepo;

    public MainActivityViewModel(){
        myRepo = new Repository();
        selectedSongId = 0;
        Log.e("created", "viewModel");
        musicItems.setValue(myRepo.getMusicItems());
        playlists.setValue(myRepo.getPlaylists());
        selectedPlaylist = new Playlist("null");
    }

    /*
    **Getters
     */
    public int getSelectedSongId(){
        return this.selectedSongId;
    }
    public ArrayList<MusicItem> getSelectedList(){return this.selectedList;}
    public MutableLiveData<ArrayList<MusicItem>> getMusicItems(){return this.musicItems;}
    public ArrayList<Playlist> getPlaylists(){return this.playlists.getValue();}
    public MutableLiveData<ArrayList<Playlist>> getPlaylistMutableLiveData(){return playlists;}
    public String[] getHomeMenuItems(){return myRepo.getHomeMenuItems();}
    public MusicItem getCurrentSong(){
        return this.currentSong.getValue();
    }
    public MutableLiveData<MusicItem> getCurrentSongMLD(){return this.currentSong;}

    public Playlist getPlaylist(Playlist playlist){
        Playlist result = null;
        for(Playlist i : getPlaylists()){
            if(i.getName() == playlist.getName()){
                result = new Playlist("" + i.getName());
            }
        }
        return result;
    }

    public Playlist getSelectedPlaylist(){
        if(this.selectedPlaylist == null){
            Log.e("selectedPlaylist", "No playlist selected");
            return null;
        }else{
            Log.e("selectedPlaylist", "Playlist: " + selectedPlaylist.getName());
        }
        return this.selectedPlaylist;
    }

    /*
     ** Setters
     */
    public void setPlaylists(ArrayList<Playlist> input){this.playlists.setValue(input);}
    public void setSelectedList(ArrayList<MusicItem> value){
        this.selectedList = value;
        Log.e("SetSelectedList",  "Selected List:");
        printList(this.selectedList);

    }
    public void setSelectedPlaylist(Playlist playlist){
        this.selectedPlaylist = playlist;
        Log.e("Selected_Playlist", "Selected playlist: " + this.getSelectedPlaylist().getName());

        Log.e("SetSelectedPlaylist",  "Selected PlayList:");
        printList(this.selectedPlaylist.getTracks());
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

    public ArrayList<MusicItem> removeSongFromMainList(MusicItem ditch){
        //Remove from any playlists
        for(int i = 0; i < playlists.getValue().size(); i++){
            Log.e("Delete i", "Parsing " + playlists.getValue().get(i).getName());
            for(int j = 0; j < playlists.getValue().get(i).getTracks().size(); j++){
                Log.e("Delete j", "Parsing " + playlists.getValue().get(i).getTracks().get(j).getTitle());
                if(playlists.getValue().get(i).getTracks().get(j) == ditch){
                    playlists.getValue().get(i).removeTrack(playlists.getValue().get(i).getTracks().get(j));
                }
            }
        }
        //Remove from main repo
        musicItems.getValue().remove(ditch);
        musicItems.setValue(musicItems.getValue());
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
            if(target.getName() == playlists.getValue().get(i).getName()){
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
    /*Widgets*/
    public void printList(ArrayList<MusicItem> list){
        for(int i = 0; i < list.size(); i++){
            Log.e("printList " + i, "|" + list.get(i).getTitle() + "|");
        }
    }


    public void printQueue(){
        MusicItem i = new MusicItem(0, "");

    }

    public void toAllMI(){
        this.musicItems.setValue(myRepo.getMusicItems());
    }

    public void addToQueueTest(MusicItem add){

    }

    public void takeOutQueueSong(MusicItem remove){

    }

}
