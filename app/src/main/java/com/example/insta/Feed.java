package com.example.insta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


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
import java.util.Date;
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

    FeedRecyclerViewAdapter adapter;

    List<Post> list;
    View view;

    public Feed() {
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dr = firebaseDatabase.getReference();
        user = auth.getCurrentUser();
        reference = storage.getReference(user.getUid());
    }


    private void getInfo() {
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
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        helper = new InstaDatabaseHelper(getContext());
        list = new ArrayList();
        getInfo();
        return view;
    }

    private void getAllPostsFromLocalDatabase() {
        list = helper.getPosts();
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            Post temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
        adapter = new FeedRecyclerViewAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);
    }

    private void getAllPostsFromFirebase() {
        DatabaseReference databaseReference = dr.child("Users").child(user.getUid()).child("Friends");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    getFriendPosts(ds.getValue(String.class));
                }
                for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
                    Post temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
                adapter = new FeedRecyclerViewAdapter(list, view.getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getFriendPosts(final String friendUID) {
        DatabaseReference databaseReference = dr.child("Users").child(friendUID).child("Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals("Count")) {
                        continue;
                    }
                    if (count++ > 11) {
                        break;
                    }
                    final Post p = new Post();
                    p.setId(ds.child("Id").getValue(Integer.class));
                    p.setCaption(ds.child("Caption").getValue(String.class));
                    p.setLikes(ds.child("Likes").getValue(Integer.class));
                    p.setDate(ds.child("Date").getValue(String.class));
                    dr.child("Users").child(friendUID).child("Notifications").child("Likes")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(user.getUid() + " " + p.getId()) != null) {
                                        p.setLiked(true);
                                    } else {
                                        p.setLiked(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    p.setUID(friendUID);
                    list.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            fab.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            }, 4000);
        }
    }
}