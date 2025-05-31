package com.freire.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeMenuItemsAdapter extends RecyclerView.Adapter<HomeMenuItemsAdapter.HomeMenuItemViewHolder> {
    private String[] items;
    //private Mediator mediator;
    MainActivity activity;
    public HomeMenuItemsAdapter(String[] items, MainActivity mainActivity){//Mediator mediator){
        this.activity = mainActivity;                        //this.mediator = mediator;
        this.items = items;
    }
    @NonNull
    @Override
    public HomeMenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_menu_item, parent, false);
        return new HomeMenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMenuItemViewHolder holder, int position) {
        holder.bind(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public class HomeMenuItemViewHolder extends RecyclerView.ViewHolder{
        TextView txtView;
        public HomeMenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.home_menu_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (getAbsoluteAdapterPosition()){
                        case 0: //playlists
                            activity.toPlaylists();
                            break;
                        case 1: //tracks
                            activity.toTracks();
                            activity.getMainActivityViewModel().setSelectedPlaylist(new Playlist("null")); //resets playlist reference
                            break;
                        case 2: //artists
                            activity.toArtistsFragment();
                            break;
                        case 3: //genre
                            activity.toGenreFragment();
                            break;
                    }
                }
            });
        }
        public void bind(String value){
            txtView.setText(value);
        }
    }
}
