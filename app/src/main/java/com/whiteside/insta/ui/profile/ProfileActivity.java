package com.whiteside.insta.ui.profile;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.whiteside.insta.databinding.FragmentViewProfileBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;


public class ProfileActivity extends Fragment {


    private final String UID;
    private final FirebaseAuth auth;
    private final Map<String, Post> posts;
    private final FirebaseFirestore fStore;
    private PostsRecyclerViewAdapter adapter;
    private List<Post> list;
//    private ImageView profileImage;
//    private TextView name, bio;
    private Profile profile;
    private FragmentViewProfileBinding bind;


    public ProfileActivity() {
        posts = new HashMap<>();
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        UID = auth.getUid();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        bind = FragmentViewProfileBinding.inflate(inflater);

        RecyclerView recyclerView = bind.profilePosts;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(bind.getRoot().getContext()));
        adapter = new PostsRecyclerViewAdapter(list, getContext());
        recyclerView.setAdapter(adapter);

        getInfo();
        getProfilePostsFromFirebaseFireStore();

        return bind.getRoot();
    }

    private void getInfo() {
        fStore.collection("Users")
                .document(UID)
                .addSnapshotListener((ds, e) -> {
                    try {
                        profile = new Profile();
                        profile = ds.toObject(Profile.class);
                        byte[] bytes = profile.getProfilePic().toBytes();
                        bind.userProfilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        bind.userProfileName.setText(profile.getName());
                        bind.userProfileShortBio.setText(profile.getBio());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
    }

    private void getProfilePostsFromFirebaseFireStore() {
        fStore.collection("Users")
                .document(UID)
                .collection("Posts")
                .addSnapshotListener((qs, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        if (!qs.getDocuments().isEmpty()) {
                            for (DocumentSnapshot ds : qs.getDocuments()) {
                                final Post p = ds.toObject(Post.class);
                                p.setLiked(p.getLikes().containsKey(auth.getUid()));
                                posts.put(p.getID(), p);

                                list = new ArrayList<>();
                                list.addAll(posts.values());
                                Collections.sort(list);
                                adapter.update(list);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Posts", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}