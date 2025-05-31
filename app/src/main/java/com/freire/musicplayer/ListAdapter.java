package com.freire.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MusicViewHolder> {
    TracksFragment tracksFragment;
    PlaylistContent playlistContent;
    public ArrayList<MusicItem> music;
    public Mediator mediator;
    public MusicViewHolder myViewHolder;
    private LifecycleOwner lifeCycleOwner;
    private String[] spinnerItems = {"Add to playlist", "Add to queue", "Delete"};

    public ListAdapter(TracksFragment tracksFragment, ArrayList<MusicItem> music, Mediator context){
        mediator = context;
        this.tracksFragment = tracksFragment;
        this.music = music;
        lifeCycleOwner = tracksFragment.getViewLifecycleOwner();
    }
    public ListAdapter(PlaylistContent playlistsFragment, ArrayList<MusicItem> tracks, Mediator context){
        mediator = context;
        this.playlistContent = playlistsFragment;
        this.music = tracks;
        lifeCycleOwner = playlistsFragment.getViewLifecycleOwner();
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_display, parent, false);
        myViewHolder = new MusicViewHolder(view);
        if(tracksFragment != null) {
            mediator.getMainActivityViewModel().getCurrentSongMLD().observe(tracksFragment.getViewLifecycleOwner(), tracksFragment.currentSongObserver);
            playlistContent = null;
        }else if(playlistContent != null) {
            mediator.getMainActivityViewModel().getCurrentSongMLD().observe(playlistContent.getViewLifecycleOwner(), playlistContent.currentSongObserver);
            tracksFragment = null;
        }
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        //bind view holder to recyclerview
        holder.bind(music.get(position));
        if(mediator.getMainActivityViewModel().getCurrentSong() != null){
           if(mediator.getMainActivityViewModel().getCurrentSong().getTitle() == holder.name.getText() && music.indexOf(mediator.getMainActivityViewModel().getCurrentSong()) == position)
                holder.name.setTextColor(Color.GREEN);
        }
     }
    @Override
    public int getItemCount() {
        return music.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        //Create View variables here
        public ImageView musicImg;
        public TextView name;
        public ImageButton options;

        MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            musicImg = itemView.findViewById(R.id.music_img);
            name = itemView.findViewById(R.id.title_view);
            options = itemView.findViewById(R.id.playlistContent_options_button);

            if(tracksFragment != null) {//tracksFragment side of things
                //options
                options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tracksFragment.mediator.popupMenu(options, new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                tracksFragment.mediator.onMenuItemClick(menuItem, music.get(getAbsoluteAdapterPosition()).getId(), music.get(getAbsoluteAdapterPosition()));

                                return false;
                            }
                        });
                        Log.e("options", "button clicked");
                    }
                });
            }else if(playlistContent != null) { //playlist music side of things
                //options
                options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playlistContent.mediator.popupMenu(options, new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                playlistContent.mediator.onMenuItemClick(menuItem, music.get(getAbsoluteAdapterPosition()).getId(), music.get(getAbsoluteAdapterPosition()));
                                Log.e("menuItemClicked", "clicked menu item " + getAbsoluteAdapterPosition());
                                return false;
                            }
                        });
                    }
                });
            }

                itemView.setOnClickListener(new View.OnClickListener() { //every time you click on a musicItem view, this happens.
                                                @Override
                                                public void onClick(View view) {
                                                    MusicItem e = new MusicItem(90, "new");
                                                    mediator.getMainActivityViewModel().setCurrentSong(music.get(getAbsoluteAdapterPosition()));
                                                    if(mediator.getMainActivityViewModel().getSelectedPlaylist().getName() != "null"){ //for when selectedlist is an actual playlist
                                                        Log.e("notEmpty_Playlist", "Populated Playlist");
                                                        mediator.getMainActivityViewModel().setSelectedList(mediator.getMainActivityViewModel().getSelectedPlaylist().getTracks());
                                                        mediator.getMusicTab();
                                                    }else { //for when selectedlist is the main tracks fragment
                                                        Log.e("Nulled_Playlist", "nulled playlist");
                                                        mediator.getMainActivityViewModel().setSelectedList(mediator.getMainActivityViewModel().getMusicItems().getValue());
                                                        Log.e("Song pressed", "Current Song: " + mediator.getMainActivityViewModel().getCurrentSong().getTitle());
                                                        mediator.getMusicTab();
                                                    }
                                                    mediator.play(mediator.getMainActivityViewModel().getCurrentSong().getTitle());
                                                }

                });
            }

        public void bind(MusicItem musicItem){
            //bind Views to variable data
            name.setText(musicItem.getTitle());
        }
    }
}
