package com.freire.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GenreFragment extends Fragment {
    private RecyclerView genreItems;
    private Mediator mediator;
    private GenreAdapter adapter;
    private ArrayList<String> genres = new ArrayList<String>();
    public GenreFragment() {
        // Required empty public constructor
    }


    public static GenreFragment newInstance() {
        GenreFragment fragment = new GenreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int i = 0; i < 3; i++){
            genres.add("Genre " + i);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_genre, container, false);
        genreItems = layout.findViewById(R.id.genreItems);
        genreItems.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        genreItems.addItemDecoration(new DividerItemDecoration(layout.getContext(), LinearLayoutManager.VERTICAL));
        adapter = new GenreAdapter(this, mediator, genres);
        genreItems.setAdapter(adapter);

        return layout;
    }
}