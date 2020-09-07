package com.example.insta;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import Controller.FriendRequestsRecyclerViewAdapter;

public class Notifications extends Fragment {

    private FloatingActionButton fab;
    private View view;


    List<String> uids;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference dbr;
    FirebaseDatabase db;
    StorageReference sr;
    FirebaseStorage fs;

    RecyclerView notifs;

    public Notifications() {
        this.uids = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbr = db.getReference();
        fs = FirebaseStorage.getInstance();
        sr = fs.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        notifs = view.findViewById(R.id.recycler_view_notifications);
        notifs.setLayoutManager(new LinearLayoutManager(view.getContext()));
        notifs.setHasFixedSize(true);
        getNotifications();
        return view;
    }

    private void getNotifications() {
        dbr.child("Users").child(user.getUid()).child("FriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s: snapshot.getChildren()) {
                    uids.add(s.child("From").getValue(String.class));
                }
                notifs.setAdapter(new FriendRequestsRecyclerViewAdapter(uids, view.getContext()));
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
            fab.setVisibility(View.INVISIBLE);
        }
    }
}