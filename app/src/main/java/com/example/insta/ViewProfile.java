package com.example.insta;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

import Controller.InstaDatabaseHelper;
import Controller.ProfileRecyclerViewAdapter;
import Model.Post;
import Model.User;


public class ViewProfile extends Fragment {

    List<Post> list;

    private String uid;

    RecyclerView recyclerView;

    InstaDatabaseHelper helper;

    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference reference;
    FirebaseAuth auth;
    FirebaseUser user;


    ImageView profilePic;
    TextView name, bio;

    View view;
    private ProfileRecyclerViewAdapter adapter;


    public ViewProfile() {
        list = new ArrayList();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference().child("Users").child(uid);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users").child(uid);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        profilePic = view.findViewById(R.id.view_profile_photo);
        name = view.findViewById(R.id.view_profile_user_name);
        bio = view.findViewById(R.id.view_profile_user_bio);

        recyclerView = view.findViewById(R.id.view_profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getInfo();

        return view;
    }


    private void getInfo() {
        list = new ArrayList();

        helper = new InstaDatabaseHelper(view.getContext());
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            getName();
            getBio();
            getProfilePic();
            getProfilePostsFromFirebase();

            helper = new InstaDatabaseHelper(getContext());
            if (!helper.checkUser(uid)) {
                final User user = new User();
                reference.child("Profile Pic").getBytes(1024 * 1024)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] b) {
                                user.setProfilePic(BitmapFactory.decodeByteArray(b, 0, b.length));
                                user.setName(name.getText().toString());
                                user.setBio(bio.getText().toString());
                                user.setEmail(auth.getCurrentUser().getEmail());
                                user.setUid(uid);
                                helper.insertUser(user);
                            }
                        });
            }

        } else {
            User user;
            user = helper.getUser(auth.getCurrentUser().getUid());
            if (user.getProfilePic() != null) {
                profilePic.setImageBitmap(user.getProfilePic());
            }
            name.setText(user.getName());
            bio.setText(user.getBio());
            getProfilePostsFromLocalDatabase();
        }
    }

    private void getName() {
        databaseReference.child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str = snapshot.getValue().toString();
                helper.updateUserName(str, uid);
                name.setText(str);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getBio() {
        databaseReference.child("Bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue().toString();
                helper.updateUserBio(s, auth.getCurrentUser().getUid());
                bio.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProfilePic() {
        reference.child("Profile Pic").getBytes(1024 * 1024).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bitmap);
                        helper.updateUserProfilePic(bytes, user.getUid());
                    }
                });
    }

    private void getProfilePostsFromLocalDatabase() {
        list = helper.getUserPosts(user.getUid());
        adapter = new ProfileRecyclerViewAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);
    }

    private void getProfilePostsFromFirebase() {
        final DatabaseReference postsReference = databaseReference.child("Posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals("Count")) {
                        continue;
                    }
                    Post p = new Post();
                    p.setId(ds.child("Id").getValue(Integer.class));
                    p.setCaption(ds.child("Caption").getValue(String.class));
                    p.setDate(ds.child("Date").getValue(String.class));
                    p.setLikes((int) ds.child("Likes").getChildrenCount());
                    p.setLiked(false);
                    p.setUID(auth.getUid());
                    list.add(p);
                }
                adapter = new ProfileRecyclerViewAdapter(list, view.getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}