package com.freire.musicplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.HomeItemsHolder>{

    public Context context;
    PlaylistsFragment playlistsFragment;
    public ArrayList<Playlist> playlists;
    public Mediator mediator;

    public PlaylistAdapter(PlaylistsFragment newFragment, ArrayList<Playlist> playlistsArray, Mediator main){
        this.playlistsFragment = newFragment;
        this.playlists = playlistsArray;
        mediator = main;
    }

    @NonNull
    @Override
    public HomeItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_display, parent, false);
        return new HomeItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemsHolder holder, int position) {
        //bind view holder to recyclerview
        holder.bind(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class HomeItemsHolder extends RecyclerView.ViewHolder {
        //Create View variables here
        public TextView view;
        private ImageButton options;

        HomeItemsHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.playlist_name);
            options = itemView.findViewById(R.id.playlistContent_options_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Playlist selected = playlists.get(getAdapterPosition());
                    playlistsFragment.getPlaylist(selected);
                    // SWITCH TO PLAYLISTCONTENT FRAGMENT.
                    //PASS selected TO MAIN FRAGMENT selectedPlaylist VARIABLE"
                    mediator.getMainActivityViewModel().setSelectedPlaylist(selected);
                    mediator.toPlaylistContent();
                    Log.e("log", "Selected playlist: " + mediator.getMainActivityViewModel().getSelectedPlaylist().getName());
                }
            });
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediator.playlistPopupMenu(view, new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            mediator.getMainActivityViewModel().setSelectedPlaylist(playlists.get(getAdapterPosition()));
                            mediator.onPlaylistMenuItemClick(menuItem);
                            return false;
                        }
                    });
                }
            });
        }

        public void bind(Playlist itemsHolder){
            //bind Views to variable data
            view.setText(itemsHolder.getName());
        }
    }
}
