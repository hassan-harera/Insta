package com.example.insta;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;

import static android.content.ContentValues.TAG;


public class ViewProfile extends Fragment {


    private String UID;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    PostsRecyclerViewAdapter adapter;
    List<Post> posts;

    ImageView profileImage;
    TextView name, bio;

    private FirebaseFirestore fStore;
    private Profile profile;


    public ViewProfile() {
        posts = new ArrayList();

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore.setFirestoreSettings(new FirebaseFirestoreSettings
                .Builder().setPersistenceEnabled(true)
                .setCacheSizeBytes(50000000).build());
        UID = auth.getUid();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        profileImage = view.findViewById(R.id.view_profile_photo);
        name = view.findViewById(R.id.view_profile_user_name);
        bio = view.findViewById(R.id.view_profile_user_bio);

        recyclerView = view.findViewById(R.id.view_profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new PostsRecyclerViewAdapter(posts, getContext());
        recyclerView.setAdapter(adapter);

        getInfo();
        getProfilePostsFromFirebaseFireStore();

        return view;
    }


    private void getInfo() {
        fStore.collection("Users")
                .document(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile = new Profile();
                        profile = ds.toObject(Profile.class);
                        byte[] bytes = profile.getProfilePic().toBytes();
                        profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        name.setText(profile.getName());
                        bio.setText(profile.getBio());
                    }
                });
    }

    private void getProfilePostsFromFirebaseFireStore() {
        fStore.collection("Users")
                .document(UID)
                .collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Error happened while download posts");
                            Log.e(TAG, task.getException().getStackTrace().toString());
                        } else {
                            if (!task.getResult().isEmpty()) {
                                for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                    final Post p = ds.toObject(Post.class);
                                    p.setLiked(p.getLikes().containsKey(auth.getUid()));
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
}