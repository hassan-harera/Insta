package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Controller.PostsRecyclerViewAdapter;
import Model.Post;

import static android.content.ContentValues.TAG;

public class VisitProfile extends AppCompatActivity {


    private String UID;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    PostsRecyclerViewAdapter adapter;
    List<Post> posts;

    ImageView profileImage;
    TextView name, bio;
    private ImageView addFriend;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_profile);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.profile_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        posts = new ArrayList<>();
        adapter = new PostsRecyclerViewAdapter(posts, this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UID = bundle.get("Token").toString();

        addFriend = findViewById(R.id.add_friend);
        if (UID.equals(auth.getUid())) {
            addFriend.setVisibility(View.INVISIBLE);
        }

        profileImage = findViewById(R.id.user_profile_photo);
        name = findViewById(R.id.user_profile_name);
        bio = findViewById(R.id.user_profile_short_bio);

        getInfo();
        getProfilePostsFromFirebaseFireStore();
    }

    private void getInfo() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        if (!isConnected) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        fStore.collection("Users")
                .document(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        byte[] bytes = ds.getBlob("Profile Pic").toBytes();
                        profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        name.setText(ds.getString("Name"));
                        bio.setText(ds.getString("Bio"));
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
                                    Post p = ds.toObject(Post.class);
                                    posts.add(p);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(VisitProfile.this, "No Posts", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void addFriendClicked(View view) {
        addFriend.setEnabled(false);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Users").child(UID)
                .child("Friend Requests");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(auth.getUid()).getValue() == null) {
                    DatabaseReference dbr = snapshot.child(auth.getUid()).getRef();
                    dbr.child("UID").setValue(auth.getUid());
                    dbr.child("Date").setValue(new Date().toString());
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