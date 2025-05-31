package com.freire.musicplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freire.musicplayer.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AddToPlaylistAdapter extends RecyclerView.Adapter<AddToPlaylistAdapter.ViewHolder>{

    private int playlist_num;
    private String playlist_title;
    private ArrayList<Playlist> playlists;
    public AddToPlaylist parentFragment;
    public AddToPlaylistAdapter(ArrayList<Playlist> items, AddToPlaylist fragment) {
        this.parentFragment = fragment;
        this.playlists = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_playlist_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(playlists.get(position));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
/*
    public Fragment getParent(){
        
    }

 */

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemNumber;
        public final TextView playlistName;
        public Playlist selected;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemNumber = view.findViewById(R.id.playlist_num);
            playlistName = view.findViewById(R.id.playlist_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = playlists.get(getAdapterPosition());
                    parentFragment.getMediator().getMainActivityViewModel().addSongToPlaylist(selected, parentFragment.getMediator().getSongToAddToPlaylist());
                    Log.e("Clicked: ", "selected playlist " + selected.getName());
                    for(int i = 0; i < parentFragment.getMediator().getMainActivityViewModel().getPlaylists().get(getAdapterPosition()).getTracks().size(); i++){
                        Log.e("playlist " + selected.getName(), "|Playlist track: " + selected.getTracks().get(i).getTitle() + "| ");
                    }
                    parentFragment.getMediator().getPublicToast().setText(parentFragment.getMediator().getSongToAddToPlaylist().getTitle() + " added to " + selected.getName());
                    parentFragment.getMediator().getPublicToast().show();
                    parentFragment.destroyFragment();
                }
            });
        }
        public void bind(Playlist itemsHolder){
            //bind Views to variable data
            itemNumber.setText("" + getAbsoluteAdapterPosition());
            playlistName.setText(itemsHolder.getName());
        }
    }
}