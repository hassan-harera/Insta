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
    private VisitProfileRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        visitedUID = bundle.get("Token").toString();

        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(visitedUID);
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users").child(visitedUID);

        addFriend = findViewById(R.id.add_friend);
        databaseReference.child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(auth.getUid()).getValue() != null){
                    addFriend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (visitedUID.equals(auth.getUid())) {
            addFriend.setVisibility(View.INVISIBLE);
        }

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new VisitProfileRecyclerViewAdapter(list, visitedUID, VisitProfile.this);
                recyclerView.setAdapter(adapter);
            }
        }, 2000);
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
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals("Count")){continue;}
                    final Post p = new Post();
                    p.setId(ds.child("Id").getValue(Integer.class));
                    p.setCaption(ds.child("Caption").getValue(String.class));
                    p.setLikes((int) ds.child("Likes").getChildrenCount());
                    p.setDate(ds.child("Date").getValue(String.class));
                    p.setLiked(ds.child("Likes").child(auth.getUid()) != null);
                    p.setUID(visitedUID);
                    list.add(p);
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}