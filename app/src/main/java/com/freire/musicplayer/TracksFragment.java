package com.freire.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Observable;

public class TracksFragment extends Fragment {
    public Mediator mediator;
    public ListAdapter adapter;
    private Spinner spinner;
    public ArrayAdapter<String> arrayAdapter;
    public static TracksFragment tracksFragment;
    private String[] spinnerItems = {"", "Add to playlist", "Add to queue", "Delete"};
    public Observer<MusicItem> currentSongObserver;
    public Observer<ArrayList<MusicItem>> listObserver;


    //popup menu
    PopupMenu popupMenu;

    public static TracksFragment newInstance() {
        return new TracksFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mediator = (Mediator) context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState) {
        Log.e("Tracks", "launched TracksFragment");
        View fragmentContainer = inflater.inflate(R.layout.tracks, container, false);
        //Set up recyclerView
        RecyclerView recyclerView = fragmentContainer.findViewById(R.id.music_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentContainer.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(fragmentContainer.getContext(), LinearLayoutManager.VERTICAL));

        adapter = new ListAdapter(this, mediator.getMainActivityViewModel().getMusicItems().getValue(), mediator);
        recyclerView.setAdapter(adapter);
        //mediator.getMainActivityViewModel().getMusicItems().observe(this.getViewLifecycleOwner(), listObserver);

        //Observers
         currentSongObserver = new Observer<MusicItem>() { //observes changes made to the current song
            @Override
            public void onChanged(MusicItem musicItem) {
                TracksFragment fragment = adapter.tracksFragment;
                adapter = new ListAdapter(fragment, mediator.getMainActivityViewModel().getMusicItems().getValue(), mediator);
                recyclerView.setAdapter(adapter);
                Log.e("currentSObserve", "currentSongObserver triggered");
            }
        };

        listObserver = new Observer<ArrayList<MusicItem>>() {
            @Override
            public void onChanged(ArrayList<MusicItem> musicItems) {
                TracksFragment fragment = adapter.tracksFragment;
                adapter = new ListAdapter(fragment, musicItems, mediator);
                recyclerView.setAdapter(adapter);
                Log.e("TracksFragObserve","tracks fragment observed a change");
            }
        };

        //mediator.getMainActivityViewModel().getMusicItems().observe(getViewLifecycleOwner(),listObserver);
        mediator.getMainActivityViewModel().getMusicItems().observe(this.getViewLifecycleOwner(), listObserver);

        return fragmentContainer;
    }

    public void showPopup(View view){
        popupMenu = new PopupMenu(this.getContext(),view);
    }
    public void getMusicItem(int songId){ mediator.onSongSelected(songId);}
    public Mediator getMediator(){return this.mediator;}
    public ArrayAdapter<String> getArrayAdapter(){return this.arrayAdapter;}
    public PopupMenu getPopupMenu(){
        return this.popupMenu;
    }
}
