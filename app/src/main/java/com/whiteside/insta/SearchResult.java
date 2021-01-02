package com.whiteside.insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

import Controller.ChatsRecyclerViewAdapter;
import Controller.SearchResultRecyclerViewAdapter;

public class SearchResult extends AppCompatActivity {

    private List<String> friends;
    private RecyclerView recyclerView;
    private ChatsRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        recyclerView = findViewById(R.id.friends);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String [] list = getIntent().getExtras().getStringArray("list");
        recyclerView.setAdapter(new SearchResultRecyclerViewAdapter(Arrays.asList(list), this));
    }
}