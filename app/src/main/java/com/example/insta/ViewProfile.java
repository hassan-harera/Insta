package com.example.insta;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

import Controller.RecyclerViewAdapter;
import Model.Post;


public class ViewProfile extends Fragment {

    List<Post> list;

    private String uid;

    RecyclerView recyclerView;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference reference;
    FirebaseAuth auth;
    FirebaseUser user;


    ImageView profilePic;
    TextView name, bio;

    View view;

    RecyclerViewAdapter recyclerViewAdapter;


    public ViewProfile() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference().child("Users").child(uid);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users").child(uid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        profilePic = view.findViewById(R.id.view_profile_photo);
        name = view.findViewById(R.id.view_profile_user_name);
        bio = view.findViewById(R.id.view_profile_user_bio);

        getProfilePic();
        getName();
        getBio();

        recyclerView = view.findViewById(R.id.view_profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPostsFromFirebase();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void getName() {
        databaseReference.child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.getValue().toString());
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
                bio.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProfilePic() {
        reference.child("Profile Pic").getBytes(4096 * 4096).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(bitmap);
            }
        });
    }

    private void getAllPostsFromFirebase() {
        list = new ArrayList<>();

        DatabaseReference postsReference = databaseReference.child("Posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post p = new Post();
                    long id = ds.child("Id").getValue(Integer.class);
                    p.setId((int) id);
                    p.setTitle(ds.child("Title").getValue(String.class));
                    p.setDetails(ds.child("Details").getValue(String.class));
                    list.add(p);
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(list, view.getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}