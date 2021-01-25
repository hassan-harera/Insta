package com.whiteside.insta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;


public class Feed extends Fragment {

    RecyclerView recyclerView;
    PostsRecyclerViewAdapter adapter;


    FirebaseAuth auth;
    FirebaseFirestore fStore;


    Map<String, Post> posts;
    View view;
    private Profile profile;
    private List<Post> list ;

    public Feed() {
        list = new ArrayList<>();
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
        posts = new HashMap();
        adapter = new PostsRecyclerViewAdapter(list, view.getContext());
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
                                posts.put(p.getID() ,p);

                                list = new ArrayList<>();
                                list.addAll(posts.values());
                                Collections.sort(list);
                                adapter.update(list);
                            }
                        }
                    });
        }
    }
}