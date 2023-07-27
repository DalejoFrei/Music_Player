package com.freire.musicplayer;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;


//music files are in /storage/emulated/0/Music
public class MainActivity extends AppCompatActivity implements Mediator{
    public ActivityResultLauncher<String> storagePermission;
    public MainActivityViewModel mainActivityViewModel;
    public Observer<ArrayList<MusicItem>> musicItemsObserver;
    public MediaPlayer music;
    public FragmentManager fragmentManager;
    public MediaMetadataRetriever mediaMetadataRetriever;
    public String artist;
    public String title;
    public Toast publicToast;
    public MusicItem toAddToPlaylist;
    private String[] homeMenuItems;
    private RecyclerView.Adapter adapter;
    private RecyclerView homeMenuItemsView;
    public TextView songName;

    public View musicTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar stuff
        //Toolbar toolbar = findViewById(R.id.)


        mainActivityViewModel = new MainActivityViewModel();
        mediaMetadataRetriever = new MediaMetadataRetriever();

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        publicToast = new Toast(this);
        fragmentManager = getSupportFragmentManager();

        //recyclerview homemenuItems
        homeMenuItems = mainActivityViewModel.getHomeMenuItems();
        homeMenuItemsView = findViewById(R.id.homeMenuItems);
        homeMenuItemsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        homeMenuItemsView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        //set recycler adapter
        adapter = new HomeMenuItemsAdapter(this.homeMenuItems, this);
        homeMenuItemsView.setAdapter(adapter);

        String path = this.getFilesDir().getAbsolutePath();
        File file = new File("save_my_life.m4a");//path+"/src/main/res/raw/save_my_life.m4a");
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contents = new String(bytes);

        music = MediaPlayer.create(this, R.raw.save_my_life);
        mainActivityViewModel.setMusic(music);
        /*
        mediaMetadataRetriever.setDataSource("" + file.getPath());
        artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        
         */


        //views
        musicTab = findViewById(R.id.music_tab_layout);
        songName = musicTab.findViewById(R.id.songName);
        ImageButton prev = musicTab.findViewById(R.id.prev);
        ImageButton playpause = musicTab.findViewById(R.id.play_pause);
        ImageButton next = musicTab.findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.pause();
                previous(getMainActivityViewModel().getCurrentSong().getTitle());
                songName.setText(getMainActivityViewModel().getCurrentSong().getTitle());
                music.start();
            }
        });
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play data in currentSong file variable
                //music.start();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.stop();
                Log.e("before clicked", "currnt song is " + getMainActivityViewModel().getCurrentSong().getTitle());
                next(getMainActivityViewModel().getCurrentSong().getTitle());
                songName.setText(getMainActivityViewModel().getCurrentSong().getTitle());
                Log.e("after clicked", "currnt song is " + getMainActivityViewModel().getCurrentSong().getTitle());
                music.start();
            }
        });
        musicTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMusicPlayerFragment();
                musicTab.setVisibility(View.INVISIBLE);
            }
        });

        Log.e("file set", "" + title);
        if(savedInstanceState != null){
            mainActivityViewModel.selectedSongId = savedInstanceState.getInt("SONG_ID");
        }

        //create the fragmentmanager FOR USE OF ORIENTATION CHANGES??
        FragmentManager musicFragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.container) != null){
            //if in portrait mode, commit synchronously
            musicFragmentManager.beginTransaction().replace(R.id.container, PlaylistsFragment.newInstance()).commitNow();
        }else{
            //if in landscape:
            Log.e("notice", "landscape mode detected");
        }

        //set observers:
        mainActivityViewModel.getMusicItems().observe(this, new Observer<ArrayList<MusicItem>>() {
            @Override
            public void onChanged(ArrayList<MusicItem> musicItems) {
                Log.e("MainActObserver", "Observed a change");
                //adjustPlaylists();
            }
        });
        if(getMainActivityViewModel().getCurrentSong() != null){
            getMusicTab();
            Log.e("musicTabLog", "currentSong is NOT null");
        }else {Log.e("musicTabLog", "currentSong is indeed null");}

        }

    @SuppressLint("ResourceType")
    public void getMusicTab(){
        musicTab = (LinearLayout) findViewById(R.id.music_tab_layout);
        musicTab.setVisibility(View.VISIBLE);
        songName.setText(getMainActivityViewModel().getCurrentSong().getTitle());

    }

    /*
    ** Fragment manager actions
     */

    public void eraseDuplicateFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ArrayList<String> fragmentIDs = new ArrayList<>();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){ //populate array with all backstack entries
            fragmentIDs.add(fragmentManager.getBackStackEntryAt(i).getName());
            Log.e("backstackname", "backstack item name is " + fragmentManager.getBackStackEntryAt(i));
        }
        if(fragmentManager.getBackStackEntryCount() > 1) {
            Log.e("stack count ", fragmentManager.getBackStackEntryCount() + " items are in the back stack");

            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                int count = 0;
                for (int j = 0; j < fragmentManager.getBackStackEntryCount(); j++) {
                    if (fragmentIDs.get(i) == fragmentIDs.get(j)) {
                        count++;
                        if(count > 1) {
                            fragmentManager.popBackStackImmediate(fragmentIDs.get(j), 0);
                            Log.e("detect", "removed stack id " + fragmentIDs.get(j) + " at position " + j);
                            Log.e("fragment count ", "positron j at " + j + " and i is " + i);
                        }
                    }
                }
            }
        }
    }
    public void destroyFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().detach(fragment).commitNow();
        fragmentManager.popBackStack();
    }
    public PlaylistsFragment toPlaylists(){
        FragmentManager playlistFragmentManager = getSupportFragmentManager();
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();
        playlistFragmentManager.beginTransaction().replace(R.id.container, playlistsFragment.newInstance()).addToBackStack("toPlaylists").commit();

        //eraseDuplicateFragments();

        return playlistsFragment;
    }
    public TracksFragment toTracks(){
        FragmentManager tracksFragmentManager = getSupportFragmentManager();
        TracksFragment tracksFragment = new TracksFragment();
        tracksFragmentManager.beginTransaction().replace(R.id.container, tracksFragment.newInstance()).addToBackStack("toTracks").setReorderingAllowed(true).commit();
        return tracksFragment;
    }
    public MusicPlayerFragment toMusicPlayerFragment(){
        FragmentManager playerFragmentManager = getSupportFragmentManager();
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        playerFragmentManager.beginTransaction().replace(R.id.container, fragment.newInstance()).addToBackStack("toPlayerFragment").commit();
        return fragment;
    }
    public PlaylistContent toPlaylistContent(){
        FragmentManager contentFragmentManager = getSupportFragmentManager();
        PlaylistContent fragment = new PlaylistContent();
        contentFragmentManager.beginTransaction().replace(R.id.container, fragment.newInstance()).addToBackStack("toPlaylistContent").commit();
        //eraseDuplicateFragments();
        return fragment;
    }

    public AddToPlaylist toAddToPlaylist(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddToPlaylist fragment = new AddToPlaylist();
        fragmentManager.beginTransaction().replace(R.id.container, fragment.newInstance()).addToBackStack("addToPlaylist").commit();
        //eraseDuplicateFragments();
        return fragment;
    }
    public ArtistsFragment toArtistsFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ArtistsFragment fragment = new ArtistsFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment.newInstance()).addToBackStack("artistsFragment").commit();
        //eraseDuplicateFragments();
        return fragment;
    }
    public GenreFragment toGenreFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        GenreFragment fragment = new GenreFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment.newInstance()).addToBackStack("GenreFragment").commit();
        //eraseDuplicateFragments();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("put", mainActivityViewModel.selectedSongId);
    }

    public void getPermissions(){

    }


    /*
    ** Getters
     */
    public MainActivityViewModel getMainActivityViewModel() {
        return this.mainActivityViewModel;
    }
    public ArrayList<MusicItem> getMusicItems(){
        return this.mainActivityViewModel.getMusicItems().getValue();
    }

    public ArrayList<File> getMusic(File dir){//populates our music data structure
        /*method that extracts music from a directory of audio files
         **Prerequisite: dir must be a directory
         */
        ArrayList<File> result = new ArrayList<>();
        File[] allFiles = dir.listFiles();
        for(File file : allFiles){
            if(file.isDirectory() && !file.isHidden()){//if file is another directory, replace result with this dir
                result.addAll(getMusic(file));
            }else{
                //if tmp = music file, check type
                if(file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")){
                    result.add(file);
                    Log.e("tag", "found something");
                }
            }
        }
        return result;
    }

    public MusicItem getSongToAddToPlaylist(){return this.toAddToPlaylist;}


    public void showSongs(){
        final ArrayList<File> songs = getMusic(Environment.getExternalStorageDirectory());
        String[] items = new String[songs.size()];
        for(int i = 0; i < items.length; i++){
            items[i] = songs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");

        }
    }

    public MusicItem setSongToAddToPlaylist(MusicItem musicItem){
        this.toAddToPlaylist = musicItem;
        return this.toAddToPlaylist;
    }
    /*
     **Queue management
     */
    public LinkedList<MusicItem> addToQueue(LinkedList<MusicItem> queue, MusicItem songToBeAdded){
        LinkedList<MusicItem> result = queue;
        if(queue == null){
            /*
            queue = new LinkedList<>();
            queue.addFirst(mainActivityViewModel.getCurrentSong());
             */
            result = new LinkedList<>();
            result.addFirst(songToBeAdded);
            Log.e("New queue", "Queue created");
   
            return result;
        }else {
            MusicItem tmp = result.getLast().getNextSong();
            songToBeAdded.setNextSong(tmp);
            songToBeAdded.setPreviousSong(result.getFirst().getPreviousSong());
            result.getLast().setNextSong(songToBeAdded);
            result.add(songToBeAdded);
            printQueue(result);
            /*
            MusicItem tmp = queue.getLast().getNextSong();
            songToBeAdded.setNextSong(tmp);
            songToBeAdded.setPreviousSong(queue.getFirst().getPreviousSong());
            queue.getLast().setNextSong(songToBeAdded);
            queue.add(songToBeAdded);
            printQueue();
             */
        }
        return result;

    }
    public LinkedList<MusicItem> popFromQueue(LinkedList<MusicItem> queue){
        MusicItem tmp = queue.getFirst();//assign head node to tmp
        //queue.addFirst(queue.getFirst().getNextSong());//assign next node to head
        queue.pop();//pop from queue
        queue.getFirst().setPreviousSong(tmp.getPreviousSong());//link head to rest of the list
        return queue;
    }
    public void printQueue(LinkedList<MusicItem> queue){
        for(int i = 0; i < queue.size(); i++){
            Log.e("Print Queue", "|" + queue.get(i).getTitle() + "|");
        }
    }
    public void resetQueue(){mainActivityViewModel.getQueue().setValue(null);}

    /*
    ** Widget tools
     */
    public void onSongSelected(int id){
        mainActivityViewModel.selectedSongId = id;

        //create fragment managers
        fragmentManager = getSupportFragmentManager();

        //link fragment manager to fragments
        fragmentManager.findFragmentById(R.id.musicFragment);
        View view = findViewById(R.id.musicFragment);

        //begin transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, MusicPlayerFragment.newInstance());
        transaction.addToBackStack("music page");
        transaction.commit();
        play(getMainActivityViewModel().getCurrentSong().getTitle());
    }
    public MusicItem songSeeker(int name){
        MusicItem result;
        for(int i = 0; i < mainActivityViewModel.getMusicItems().getValue().size(); i++){
            result = mainActivityViewModel.getMusicItems().getValue().get(i);
            if(result.getId() == name){
                Log.e("found", "Found song " + result.getTitle());
                return result;
            }
            Log.e("!found", "" + name + " not found n=" + i + " Compared to " + result.getTitle());
        }
        return null;
    }


    private boolean checkInstance(MusicItem check, ArrayList<MusicItem> list){
        //checks for an instance of a MusicItem in a list. Returns true if it exists, false if it's not there
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) == check) {
                Log.e("check", "CheckedInstance true");
                return true;
            }
        }
        Log.e("check", "CheckedInstance false");

        return false;
    }
    public Toast getPublicToast(){return this.publicToast;}

    /*
    **Options menu
     */
    public void popupMenu(View view,PopupMenu.OnMenuItemClickListener listener){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.inflate(R.menu.track_menu_items);
        popupMenu.show();
    }
    public void playlistPopupMenu(View view,PopupMenu.OnMenuItemClickListener listener){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.inflate(R.menu.playlist_menu_items);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item, int position, MusicItem song){
        switch (item.getItemId()){
            case R.id.item1: //add to queue
                Log.e("Menu item selected", "Selected " + item.getTitle() + " | Position: " + position);
                mainActivityViewModel.getQueue().setValue(addToQueue(this.mainActivityViewModel.getQueue().getValue(), song));
                //addToQueue(this.mainActivityViewModel.getQueue().getValue(), song);
                mainActivityViewModel.getQueue();
                return true;
            case R.id.item2: //add to playlist
                Log.e("Menu item selected", "Selected " + item.getTitle() + " | Position: " + position);
                this.toAddToPlaylist = song;
                toAddToPlaylist();
                return true;
            case R.id.item3: //delete
                //mainActivityViewModel.removeSong(song);
                //delete from playlists
                mainActivityViewModel.removeFromPlaylist(song);
                /*
                if(getSupportFragmentManager().findFragmentByTag("toTracks") != null) {
                    for (int i = 0; i < mainActivityViewModel.getPlaylists().size(); i++) {//playlist
                        Log.e("for Playlist", "currently on " + mainActivityViewModel.getPlaylists().get(i).getName());
                        for (int j = 0; j < mainActivityViewModel.getPlaylists().get(i).getTracks().size(); j++) {//playlist tracks
                            Log.e("for playlistTracks", "currently on " + mainActivityViewModel.getPlaylists().get(i).getTracks().get(j).getTitle());
                            if (mainActivityViewModel.getPlaylists().get(i).getTracks().get(j).getId() == song.getId()) {
                                //delete the song from the playlist
                                getMainActivityViewModel().getPlaylists().get(i).removeTrack(getMainActivityViewModel().getPlaylists().get(i).getTracks().get(j));
                            }
                        }
                    }
                }

                 */
                //mainActivityViewModel.getMusicItems().postValue(get);
                Log.e("Menu item selected", "Selected " + item.getTitle() + " | Position: " + position);
                return true;
        }
        return false;
    }

    @Override
    public boolean onPlaylistMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1: //Edit
                Log.e("Menu item selected", "Selected " + item.getTitle());
                //addToQueue(this.mainActivityViewModel.getQueue().getValue(), song);
                mainActivityViewModel.getQueue();
                return true;
            case R.id.item2: //add to queue
                Log.e("Menu item selected", "Selected " + item.getTitle());
                for(int i = 0; i < getMainActivityViewModel().getSelectedPlaylist().getTracks().size(); i++){
                    addToQueue(getMainActivityViewModel().getQueue().getValue(), getMainActivityViewModel().getSelectedPlaylist().getTracks().get(i));
                }
                return true;
            case R.id.item3: //delete
                //mainActivityViewModel.getMusicItems().postValue(get);
                getMainActivityViewModel().removePlaylist(getMainActivityViewModel().getSelectedPlaylist());
                Log.e("Menu item selected", "Selected " + item.getTitle());
                //destroyFragment(fragment);
                if(getFragEntries("toPlaylistContent") == true) {
                    fragmentManager.popBackStack();
                }
                return true;
        }
        return false;
    }
    private boolean getFragEntries(String tag){
        //provides data on fragment entries
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
            if(fragmentManager.getBackStackEntryAt(i).getName() == tag)
                return true;
        }
        return false;
    }
    /*
    ** MusicItem actions
     */
    public void play(String title){
        music.start();
        Log.e("played", "playing music...." + title);
    }
    public void pause(String title){
        music.pause();
        Log.e("paused", "music paused");
    }
    public void stop(String title){
        music.stop();
        Log.e("stopped", "Music stopped");
        music = MediaPlayer.create(this, R.raw.over_the_horizon);
        Log.e("msg", "created new music instance");
    }

    public void next(String title){
        MusicItem tmp = mainActivityViewModel.getCurrentSong();
        if(mainActivityViewModel.getCurrentSong().getNextSong() != null) {
            music.stop();
            Log.e("fr", "next song button");
            mainActivityViewModel.setCurrentSong(mainActivityViewModel.getCurrentSong().getNextSong());
            mainActivityViewModel.getCurrentSong().setPreviousSong(tmp);
            music = MediaPlayer.create(this, R.raw.save_my_life);
            play(getMainActivityViewModel().getCurrentSong().getTitle());
            Log.e("test song", "song: " + mainActivityViewModel.getCurrentSong().getTitle());
            for (int i = 0; i < mainActivityViewModel.getMusicItems().getValue().size(); i++) {
                Log.e("Print musicItems", "|" + mainActivityViewModel.getMusicItems().getValue().get(i).getTitle() + "|");
            }
        }else{
            Log.e("uh oh", "next song is null");
        }
        //USE VIEWMODEL & LIVEDATA TO CHANGE VIEW
    }
    public void previous(String title){
        Log.e("prev", "previous button");
        if(mainActivityViewModel.getCurrentSong().getPreviousSong() != null) {
            mainActivityViewModel.setCurrentSong(mainActivityViewModel.getCurrentSong().getPreviousSong());
        }else {
            Log.e("null prev song", "Prevsong is null");
        }
    }
}