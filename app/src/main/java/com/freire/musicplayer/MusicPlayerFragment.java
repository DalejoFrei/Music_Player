package com.freire.musicplayer;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicPlayerFragment extends Fragment{
    private Mediator mediator;

    public ImageButton prev;
    public ImageButton pause;
    public ImageButton play;
    public ImageButton next;

    public ImageView background;
    public TextView songName;
    public SeekBar progress;

    private ImageButton settings;
    private String action0 = "";
    private String action1 = "Add to Playlist";
    private String action2 = "Add to Queue";
    private String action3 = "Delete";

    private boolean lock = false;
    public ArrayList<MusicItem> musicItems;


    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerFragment newInstance() {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mediator = (Mediator)context;
    }
    @Override
    public void onPause() {
        super.onPause();
        mediator.getMusicTab();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediator.getMusicTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_view, container, false);
        musicItems = mediator.getMusicItems();
//        music = MediaPlayer.create(getContext(), R.raw.smells_like_teen_spirit);

        //bind views to vars
        prev = view.findViewById(R.id.rewind_button);
        pause = view.findViewById(R.id.pause_button);
        play = view.findViewById(R.id.play_button);
        next = view.findViewById(R.id.forward_button);
        background = view.findViewById(R.id.music_image);
        songName = view.findViewById(R.id.song_name);

        progress = view.findViewById(R.id.seekBar);

        settings = view.findViewById(R.id.settings_button);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });


/*
        MusicItem newItem = new MusicItem(0, "Over the Horizon");
        musicItems.add(newItem);
        music = MediaPlayer.create(this,R.raw.over_the_horizon);

 */

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        songName.setText(mediator.getMainActivityViewModel().getCurrentSong().getTitle());
        Log.e("show name", "" + mediator.getMainActivityViewModel().getCurrentSong().getTitle());

        //Settings setup
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediator.setSongToAddToPlaylist(mediator.getMainActivityViewModel().getCurrentSong());
                mediator.popupMenu(view, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mediator.onMenuItemClick(menuItem, menuItem.getItemId(), mediator.getMainActivityViewModel().getCurrentSong());
                        return false;
                    }
                });
            }
        });
    }
    public void play(){
        mediator.play(songName.getText().toString());
    }
    public void pause(){
        mediator.pause(songName.getText().toString());
    }
    public void previous(){
        mediator.previous(songName.getText().toString());
        songName.setText(mediator.getMainActivityViewModel().getCurrentSong().getTitle());

    }
    public void next(){
        mediator.next(songName.getText().toString());
        songName.setText(mediator.getMainActivityViewModel().getCurrentSong().getTitle());
    }
}
