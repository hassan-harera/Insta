package com.example.insta;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Controller.NotificationsRecyclerViewAdapter;
import Model.Notifications.FriendRequestNotification;
import Model.Notifications.LikeNotification;
import Model.Notifications.Notification;
import Model.Post;
import Model.Profile;

import static android.content.ContentValues.TAG;

public class Notifications extends Fragment {

    private View view;


    List<Notification> notifications;

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    RecyclerView recyclerView;
    private NotificationsRecyclerViewAdapter adapter;
    private Profile profile;

    public Notifications() {
        this.notifications = new ArrayList();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationsRecyclerViewAdapter(notifications, view.getContext());
        recyclerView.setAdapter(adapter);

        getNotifications();

        return view;
    }

    private void getNotifications() {
        getFriendRequests();
        getLikes();
        Collections.sort(notifications);
    }

    private void getFriendRequests() {
        fStore.collection("Users")
                .document(auth.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            profile = ds.toObject(Profile.class);

                            Map<String, Timestamp> map = profile.getFriendRequests();
                            for (String UID : map.keySet()) {
                                FriendRequestNotification n = new FriendRequestNotification();
                                n.setUID(UID);
                                n.setDate(map.get(UID));
                                notifications.add(n);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void getLikes() {
        fStore.collection("Users")
                .document(auth.getUid())
                .collection("Posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot qs,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (!qs.isEmpty()) {
                            for (DocumentSnapshot ds : qs.getDocuments()) {
                                Post p = ds.toObject(Post.class);
                                Map<String, Timestamp> likes = p.getLikes();
                                if (!likes.isEmpty()) {
                                    LikeNotification n = new LikeNotification();
                                    n.setLikeNumbers(likes.size());
                                    n.setDate(likes.get(likes.keySet().size()-1));
                                    n.setPostID(p.getID());
                                    notifications.add(n);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }
}