package com.freire.musicplayer;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.LinkedList;

public interface Mediator {

     void onSongSelected(int id);


    /*
    **Fragments
     */
    public void destroyFragment(Fragment fragment);
    public PlaylistsFragment toPlaylists();
    public TracksFragment toTracks();
    public PlaylistContent toPlaylistContent();
    public AddToPlaylist toAddToPlaylist();
    public GenreFragment toGenreFragment();
   /*
    **Getters
     */
    public ArrayList<MusicItem> getMusicItems();
    public MainActivityViewModel getMainActivityViewModel();
    public MusicItem getSongToAddToPlaylist();
    public void showSongs();
    /*
    **Setters
     */
    public MusicItem setSongToAddToPlaylist(MusicItem musicItem);
    /*
    **Menu widgets
     */
    public void popupMenu(View view, PopupMenu.OnMenuItemClickListener listener);
    public void playlistPopupMenu(View view,PopupMenu.OnMenuItemClickListener listener);
        public boolean onMenuItemClick(MenuItem item, int position, MusicItem song);
    public boolean onPlaylistMenuItemClick(MenuItem item);
        /*
    **Queue methods
     */
    public void resetQueue();
    public LinkedList<MusicItem> popFromQueue(LinkedList<MusicItem> queue);
    public LinkedList<MusicItem> addToQueue(LinkedList<MusicItem> queue, MusicItem songToBeAdded);
    /*
    **Music item actions
     */
    public void play(String title);
    public void pause(String title);
    public void stop(String title);
    public void next(String title);
    public void previous(String title);
    public void getMusicTab();

    //toast
    public Toast getPublicToast();
    public MusicItem songSeeker(int name);
    }

