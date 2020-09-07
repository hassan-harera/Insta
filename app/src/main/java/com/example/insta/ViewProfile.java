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
    FloatingActionButton fab;
    ProfileRecyclerViewAdapter profileRecyclerViewAdapter;


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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        profilePic = view.findViewById(R.id.view_profile_photo);
        name = view.findViewById(R.id.view_profile_user_name);
        bio = view.findViewById(R.id.view_profile_user_bio);

        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);



        recyclerView = view.findViewById(R.id.view_profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new InstaDatabaseHelper(view.getContext());

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getProfilePic();
            getName();
            getBio();
            getProfilePostsFromFirebase();
        } else {
            User user;
            user = helper.getUser(auth.getCurrentUser().getUid());
            if(user.getProfilePic() != null){
                profilePic.setImageBitmap(user.getProfilePic());
            }
            name.setText(user.getName());
            bio.setText(user.getBio());
            getProfilelPostsFromLocalDatabase();
        }

        if(!helper.checkUser(user.getUid())){
            User user = new User();
            user.setName(name.getText().toString());
            user.setBio(bio.getText().toString());
            user.setEmail(auth.getCurrentUser().getEmail());
            user.setUid(auth.getCurrentUser().getUid());
            InstaDatabaseHelper db = new InstaDatabaseHelper(view.getContext());
            db.insertUser(user);
        }
    }

    private void getName() {
        databaseReference.child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str = snapshot.getValue().toString();
                helper.updateUserName(str, auth.getCurrentUser().getUid());
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
        reference.child("Profile Pic").getBytes(4096 * 4096).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bitmap);
                        helper.updateUserProfilePic(bytes, user.getUid());
                    }
                });
    }

    private void getProfilelPostsFromLocalDatabase() {
        list = helper.getProfilePosts();
        recyclerView.setAdapter(new ProfileRecyclerViewAdapter(list, getContext()));
    }

    private void getProfilePostsFromFirebase() {
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
                    p.setDescription(ds.child("Details").getValue(String.class));
                    list.add(p);
                }
                recyclerView.setAdapter(new ProfileRecyclerViewAdapter(list, view.getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            fab.setVisibility(View.INVISIBLE);
        }
    }
}