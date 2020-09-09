package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Controller.VisitProfileRecyclerViewAdapter;
import Model.Post;

public class VisitProfile extends AppCompatActivity {


    List<Post> list;

    private String visitedUID;

    RecyclerView recyclerView;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference reference;
    FirebaseAuth auth;

    ImageView profilePic, addFriend;
    TextView name, bio;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        visitedUID = bundle.get("Token").toString();

        addFriend = findViewById(R.id.add_friend);

        if (visitedUID.equals(auth.getUid())) {
            addFriend.setVisibility(View.INVISIBLE);
            addFriend.setClickable(false);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(visitedUID);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users").child(visitedUID);

        profilePic = findViewById(R.id.user_profile_photo);
        name = findViewById(R.id.user_profile_name);
        bio = findViewById(R.id.user_profile_short_bio);

        getProfilePic();
        getName();
        getBio();

        recyclerView = findViewById(R.id.profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllPostsFromFirebase();

        progressBar = findViewById(R.id.prgress_bar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new VisitProfileRecyclerViewAdapter(list, visitedUID, VisitProfile.this));
                progressBar.setVisibility(View.GONE);
            }
        }, 3000);
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
                    p.setCaption(ds.child("Title").getValue(String.class));
                    list.add(p);
                }
                // instead of using sorting based time take O(N l(n)) this swishes take O(N)
                for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
                    Post temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFriendClicked(View view) {
        addFriend.setEnabled(false);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Users").child(visitedUID).child("FriendRequests").child(auth.getUid());
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("From").getValue() == null) {
                    snapshot.child("From").getRef().setValue(auth.getUid());
                    Toast.makeText(VisitProfile.this, "Request Sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VisitProfile.this, "Request Already Sent", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}