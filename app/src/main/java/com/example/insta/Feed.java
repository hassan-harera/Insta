package com.example.insta;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;

import static android.content.ContentValues.TAG;


public class Feed extends Fragment {

    RecyclerView recyclerView;
    PostsRecyclerViewAdapter adapter;


    FirebaseAuth auth;
    FirebaseFirestore fStore;


    List<Post> posts;
    View view;
    private Profile profile;

    public Feed() {

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings.
                Builder().
                setCacheSizeBytes(50000000).setPersistenceEnabled(true).build());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        posts = new ArrayList<>();
        adapter = new PostsRecyclerViewAdapter(posts, view.getContext());

        getFriendsPosts();

        return view;
    }

    private void getPostsFor(String UID) {
        fStore.collection("Users")
                .document(auth.getUid())
                .collection("Posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot qs, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (!qs.isEmpty()) {
                            for (int i = qs.size() - 1; i > qs.size() - 6; i--) {
                                posts.add(qs.getDocuments().get(i).toObject(Post.class));
                            }
                        }
                    }
                });
    }

    private void getFriendsPosts() {
        profile = new Profile();
        fStore.collection("Users")
                .document(auth.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            profile = ds.toObject(Profile.class);
                            for (String UID : profile.getFriends()) {
                                getPostsFor(UID);
                            }
                        }
                    }
                });
    }
}