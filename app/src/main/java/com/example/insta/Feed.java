package com.example.insta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;

import static android.content.ContentValues.TAG;


public class Feed extends Fragment {

    RecyclerView recyclerView;
    PostsRecyclerViewAdapter adapter;



    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore fStore;


    List<Post> posts;
    View view;

    public Feed() {
        posts = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings.
                Builder().
                setCacheSizeBytes(50000000).setPersistenceEnabled(true).build());


        user = auth.getCurrentUser();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PostsRecyclerViewAdapter(posts, view.getContext());

        getFriendsPosts();

        return view;
    }

    private void getUserPostsFromFireStore() {
//        DatabaseReference databaseReference = dr.child("Users").child(user.getUid()).child("Friends");
        fStore.collection("Users").document(user.getUid()).collection("Posts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error happened while download posts");
                    Log.e(TAG, task.getException().getStackTrace().toString());
                } else {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            Post p = ds.toObject(Post.class);
                            posts.add(p);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "No Posts", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getFriendsPosts() {
        final List<String> friendsUID = new ArrayList();
        fStore.collection("Users").document(user.getUid()).collection("Friends")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error happened while download Friends");
                    Log.e(TAG, task.getException().getStackTrace().toString());
                } else {
                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                            friendsUID.add(ds.getString("UID"));
                        }
                    } else {
                        Toast.makeText(getContext(), "No Posts", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        for (String uid : friendsUID) {
            final int[] i = {1};
            fStore.collection("Users").document(uid).collection("Posts")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error happened while download posts");
                        Log.e(TAG, task.getException().getStackTrace().toString());
                    } else {
                        if (!task.getResult().isEmpty()) {
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                if(i[0] > 5)
                                    break;
                                Post p = ds.toObject(Post.class);
                                posts.add(p);
                                adapter.notifyDataSetChanged();
                                i[0]++;
                            }
                        } else {
                            Toast.makeText(getContext(), "No Posts", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}