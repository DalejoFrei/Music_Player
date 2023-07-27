package com.freire.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistContent extends Fragment {
    Mediator mediator;
    public ListAdapter adapter;
    private EditText playlistName;
    private ImageView playlistImage;
    private PlaylistContent fragment;
    private ImageButton playlistContent_options_button;
    private Observer<ArrayList<MusicItem>> playlistTracksOberserver;
    public Observer currentSongObserver;
    public PlaylistContent() {
        //required empty constructor
    }

    public PlaylistContent newInstance() {
        this.fragment = new PlaylistContent();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mediator = (Mediator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_tracks, container, false);
        playlistContent_options_button = view.findViewById(R.id.playlistContent_options_button);
        playlistName = view.findViewById(R.id.content_name);
        playlistImage = view.findViewById(R.id.content_image);
        playlistName.setText(mediator.getMainActivityViewModel().getSelectedPlaylist().getName());
        RecyclerView recyclerView = view.findViewById(R.id.tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        if(!mediator.getMainActivityViewModel().getSelectedPlaylist().getTracks().isEmpty())
            playlistInit();

        playlistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                mediator.getMainActivityViewModel().getSelectedPlaylist().rename(playlistName.getText().toString());
                Log.e("Text", "set name");

            }
        });

        //Set the observer
        //set observer to change recyclerview
       playlistTracksOberserver = new Observer<ArrayList<MusicItem>>() {
            @Override
            public void onChanged(ArrayList<MusicItem> musicItems) {
                PlaylistContent fragment = adapter.playlistContent;
                adapter = new ListAdapter(fragment, musicItems, mediator);
                recyclerView.setAdapter(adapter);
            }
        };
        mediator.getMainActivityViewModel().getSelectedPlaylist().tracks.observe(this.getViewLifecycleOwner(), playlistTracksOberserver);

        // Set the adapter

        adapter = new ListAdapter(this, mediator.getMainActivityViewModel().getSelectedPlaylist().getTracks(), mediator);
        recyclerView.setAdapter(adapter);
        playlistContent_options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediator.playlistPopupMenu(view, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mediator.onPlaylistMenuItemClick(menuItem);
                        return false;
                    }
                });
            }
        });
        //highlighter observer
        currentSongObserver = new Observer<MusicItem>() { //observes changes made to the current song
            @Override
            public void onChanged(MusicItem musicItem) {
                adapter = new ListAdapter(adapter.playlistContent, mediator.getMainActivityViewModel().getSelectedPlaylist().getTracks(), mediator);
                recyclerView.setAdapter(adapter);
            }
        };
        return view;
    }
    public void getSong(int songID){mediator.onSongSelected(songID);}
    public void playlistInit(){
        ArrayList<MusicItem> playlistTracks = mediator.getMainActivityViewModel().getSelectedPlaylist().getTracks();
        if(playlistTracks.size() > 1) {
            for (int i = 0; i < playlistTracks.size()-1; i++) {
                playlistTracks.get(i).setNextSong(playlistTracks.get(i+1));
                if(i>0) {
                    playlistTracks.get(i).setPreviousSong(playlistTracks.get(i - 1));
                    Log.e("prevSong", "set previous song to " + playlistTracks.get(i).getPreviousSong().getTitle());
                }
            }
            playlistTracks.get(playlistTracks.size()-1).setNextSong(playlistTracks.get(0));
        }else{
            playlistTracks.get(0).setNextSong(playlistTracks.get(0));
            playlistTracks.get(0).setPreviousSong(playlistTracks.get(0));
        }
    }
}