package com.freire.musicplayer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {

    private Mediator mediator;
    public PlaylistAdapter adapter;
    private ImageButton addPlaylist;

    public PlaylistsFragment() {
        // Required empty public constructor
    }
    public static PlaylistsFragment newInstance() {
        PlaylistsFragment fragment = new PlaylistsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mediator = (Mediator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View fragmentContainer = inflater.inflate(R.layout.playlists, container, false);
        addPlaylist = fragmentContainer.findViewById(R.id.add_playlist_button);
        RecyclerView recyclerView = fragmentContainer.findViewById(R.id.playlist_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentContainer.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(fragmentContainer.getContext(), LinearLayoutManager.VERTICAL));
        Observer<ArrayList<Playlist>> playlistListObserver = new Observer<ArrayList<Playlist>>() {
            @Override
            public void onChanged(ArrayList<Playlist> playlists) {
                PlaylistsFragment fragment = adapter.playlistsFragment;
                adapter = new PlaylistAdapter(fragment, playlists, mediator);
                recyclerView.setAdapter(adapter);
                Log.e("Observer", "Playlists observed a change");
            }
        };
        adapter = new PlaylistAdapter(this, mediator.getMainActivityViewModel().getPlaylists(), mediator);
        recyclerView.setAdapter(adapter);
        mediator.getMainActivityViewModel().getPlaylistMutableLiveData().observe(this.getViewLifecycleOwner(), playlistListObserver);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add a playlist
                Playlist newPlaylist = new Playlist("New Playlist");
                mediator.getMainActivityViewModel().addPlaylist(newPlaylist);
                mediator.getMainActivityViewModel().setSelectedPlaylist(newPlaylist);
                mediator.toPlaylistContent();
            }
        });
        return fragmentContainer;
    }
    public Playlist getPlaylist(Playlist playlist){
        return mediator.getMainActivityViewModel().getPlaylist(playlist);
    }
}