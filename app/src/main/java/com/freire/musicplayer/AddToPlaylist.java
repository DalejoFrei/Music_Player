package com.freire.musicplayer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class AddToPlaylist extends Fragment {


    private RecyclerView.Adapter adapter;
    private Mediator mediator;
    private ImageButton backButton;
    public AddToPlaylist() {
    }

    @SuppressWarnings("unused")
    public static AddToPlaylist newInstance() {
        AddToPlaylist fragment = new AddToPlaylist();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_select_list, container, false);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destroyFragment();
                Log.e("exit button", "exit button pressed");
            }
        });
        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new AddToPlaylistAdapter(mediator.getMainActivityViewModel().getPlaylists(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }
    public void destroyFragment(){
        mediator.destroyFragment(this);
    }
    public Mediator getMediator(){return this.mediator;}
}