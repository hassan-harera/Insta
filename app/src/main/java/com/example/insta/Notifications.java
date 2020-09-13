package com.example.insta;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Controller.NotificationsRecyclerViewAdapter;
import Model.Notification;

public class Notifications extends Fragment {

    private FloatingActionButton fab;
    private View view;


    List<Notification> notifications;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference dbr;
    FirebaseDatabase db;
    StorageReference sr;
    FirebaseStorage fs;

    RecyclerView recyclerView;
    private NotificationsRecyclerViewAdapter aapter;

    public Notifications() {
        this.notifications = new ArrayList();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbr = db.getReference();
        fs = FirebaseStorage.getInstance();
        sr = fs.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aapter = new NotificationsRecyclerViewAdapter(notifications, view.getContext());
                recyclerView.setAdapter(aapter);
            }
        }, 3000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        getNotifications();
        return view;
    }

    private void getNotifications() {
        getFriendRequests(dbr.child("Users").child(user.getUid()).child("Friend Requests"));
        getLikes(dbr.child("Users").child(user.getUid()).child("Posts"));
        Collections.sort(notifications);
    }

    private void getFriendRequests(DatabaseReference friend_requests) {
        friend_requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for (DataSnapshot s : ds.getChildren()) {
                    Notification n = new Notification();
                    n.setType("Friend Request");
                    n.setUID(s.child("UID").getValue().toString());
                    n.setDate(new Date(s.child("Date").getValue().toString()));
                    notifications.add(n);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLikes(final DatabaseReference posts) {
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for (DataSnapshot s : ds.getChildren()) {
                    if (s.getKey().equals("Count")) {
                        continue;
                    }
                    s.child("Likes").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dss) {
                            for (DataSnapshot s : dss.getChildren()) {
                                Notification n = new Notification();
                                n.setType("Like");
                                n.setPostID(s.child("Post ID").getValue().toString());
                                n.setUID(s.child("UID").getValue().toString());
                                n.setDate(new Date(s.child("Date").getValue().toString()));
                                notifications.add(n);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                aapter.notifyDataSetChanged();
            }
        }, 10000);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null){
            fab.setVisibility(View.INVISIBLE);
        }
    }
}