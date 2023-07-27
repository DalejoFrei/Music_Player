package com.freire.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private GenreFragment genreFragment;
    private Mediator mediator;
    private ArrayList<String> genres;

    public GenreAdapter(GenreFragment fragment, Mediator mediator, ArrayList<String> genres){
        this.genreFragment = fragment;
        this.mediator = mediator;
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firmware, parent, false);
        return new GenreViewHolder(view);
    }
    
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.bind(genres.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        public GenreViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            image = view.findViewById(R.id.img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Playlist genre = new Playlist("");
                    for(int i = 0; i < mediator.getMainActivityViewModel().getPlaylists().size(); i++){
                        if(mediator.getMainActivityViewModel().getPlaylists().get(i).getName() == "Genres")
                            genre = mediator.getMainActivityViewModel().getPlaylists().get(i);
                    }
                    mediator.getMainActivityViewModel().setSelectedPlaylist(mediator.getMainActivityViewModel().getPlaylist(genre));
                    mediator.toPlaylistContent();
                }
            });
        }
        public void bind(String genre){
            name.setText(genre);

        }
    }
}
