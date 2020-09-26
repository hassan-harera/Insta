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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import Controller.ChatRecyclerViewAdapter;
import Controller.ChatsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;

public class Chats extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    List<String> friends;
    RecyclerView recyclerView;
    private ChatsRecyclerViewAdapter adapter;

    public Chats() {
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.friends);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getChats();

        return view;
    }

    public void getChats() {
        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        friends = (List<String>) ds.get("friends");
                        adapter = new ChatsRecyclerViewAdapter(getContext(), friends);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}