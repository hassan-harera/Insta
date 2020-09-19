package com.example.insta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Controller.ChatsRecyclerViewAdapter;

public class Chats extends Fragment {

    FirebaseAuth auth;
    DatabaseReference dbr;
    List<String> friends;
    RecyclerView recyclerView;
    ChatsRecyclerViewAdapter chats;

    public Chats() {
        auth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();
        friends = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.friends);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getChats();
        return view;
    }

    public void getChats() {
        dbr.child("Users").child(auth.getUid()).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    friends.add(s.getKey());
                }
                chats = new ChatsRecyclerViewAdapter(getContext(), friends);
                recyclerView.setAdapter(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}