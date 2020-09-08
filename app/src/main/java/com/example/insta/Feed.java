package com.example.insta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import Controller.FeedRecyclerViewAdapter;
import Controller.InstaDatabaseHelper;
import Model.Post;


public class Feed extends Fragment {

    RecyclerView recyclerView;
    InstaDatabaseHelper helper;


    FirebaseStorage storage;
    FirebaseUser user;
    StorageReference reference;
    FirebaseAuth auth;
    DatabaseReference dr;
    FirebaseDatabase firebaseDatabase;

    FloatingActionButton fab;

    List<Post> list;
    View view;

    public Feed() {
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dr = firebaseDatabase.getReference();
        user = auth.getCurrentUser();
        reference = storage.getReference(user.getEmail());
    }


    @Override
    public void onStart() {
        super.onStart();
        fab.setVisibility(View.VISIBLE);

        viewComponents();
    }

    private void viewComponents() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getAllPostsFromFirebase();
        } else {
            getAllPostsFromLocalDatabase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_feed, container, false);

        recyclerView = view.findViewById(R.id.posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        helper = new InstaDatabaseHelper(getContext());

        return view;
    }

    private void getAllPostsFromLocalDatabase() {
        list = new ArrayList();
        list = helper.getFeedPosts();
        recyclerView.setAdapter(new FeedRecyclerViewAdapter(list, getContext()));
    }

    private void getAllPostsFromFirebase() {
        list = new ArrayList();
        DatabaseReference databaseReference = dr.child("Users").child(user.getUid()).child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post p = new Post();
                    int id = ds.child("Id").getValue(Integer.class);
                    p.setId(id);
                    p.setTitle(ds.child("Title").getValue(String.class));
                    p.setDescription(ds.child("Details").getValue(String.class));
                    list.add(p);
                }
                recyclerView.setAdapter(new FeedRecyclerViewAdapter(list, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            fab.setVisibility(View.VISIBLE);
//            viewComponents();
        }
    }
}