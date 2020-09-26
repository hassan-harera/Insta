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
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        recyclerView.setAdapter(adapter);


        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile = ds.toObject(Profile.class);
                        getFriendsPosts();
                    }
                });


        return view;
    }

    private void getFriendsPosts() {
        for (String UID : profile.getFriends()) {
            fStore.collection("Users")
                    .document(UID)
                    .collection("Posts")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot qs) {
                            for (DocumentSnapshot ds : qs.getDocuments()) {
                                final Post p = ds.toObject(Post.class);
                                p.setLiked(p.getLikes().containsKey(auth.getUid()));
                                posts.add(p);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}