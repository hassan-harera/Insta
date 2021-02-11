package com.whiteside.insta;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.whiteside.insta.databinding.ActivityVisitProfileBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;
import Model.Profile;

import static android.content.ContentValues.TAG;

public class VisitProfile extends AppCompatActivity {


    private String UID;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private PostsRecyclerViewAdapter adapter;
    private Map<String, Post> posts;
    private List<Post> list;

    private FirebaseFirestore fStore;
    private Profile profile;
    private ActivityVisitProfileBinding bind;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityVisitProfileBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        profile = new Profile();
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        posts = new HashMap();
        adapter = new PostsRecyclerViewAdapter(list, this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UID = bundle.get("UID").toString();

        getInfo();
        getProfilePostsFromFirebaseFireStore();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getInfo() {
        fStore.collection("Users")
                .document(UID)
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile = ds.toObject(Profile.class);
                        bind.userProfileName.setText(profile.getName());
                        bind.userProfileShortBio.setText(profile.getBio());
                        bind.userProfilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(
                                profile.getProfilePic().toBytes(), 0, profile.getProfilePic().toBytes().length));

                        if (profile.getFriends().contains(auth.getUid())) {
                            bind.addFriend.setVisibility(View.INVISIBLE);
                        } else if (UID.equals(auth.getUid())) {
                            bind.addFriend.setVisibility(View.INVISIBLE);
                        } else {
                            bind.addFriend.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private void getProfilePostsFromFirebaseFireStore() {
        fStore.collection("Users")
                .document(UID)
                .collection("Posts")
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Error happened while download posts");
                            Log.e(TAG, task.getException().getStackTrace().toString());
                        } else {
                            if (!task.getResult().isEmpty()) {
                                for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                    Post p = ds.toObject(Post.class);
                                    posts.put(p.getID(), p);

                                    list = new ArrayList<>();
                                    list.addAll(posts.values());
                                    Collections.sort(list);
                                    adapter.update(list);
                                }
                            } else {
                                Toast.makeText(VisitProfile.this, "No Posts", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void addFriendClicked(View view) {
        profile.addFriendRequest(auth.getUid());

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(Objects.requireNonNull(UID))
                .update("friendRequests", profile.getFriendRequests())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}