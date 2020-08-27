package com.example.insta;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Controller.FirebaseDatabaseController;
import Controller.InstaDatabaseHelper;
import Controller.RecyclerViewAdapter;
import Model.Post;

public class Feed extends AppCompatActivity {

    RecyclerView recyclerView;
    InstaDatabaseHelper helper;

    FirebaseStorage storage;
    FirebaseUser user;
    StorageReference reference;
    FirebaseAuth auth;
    DatabaseReference dr;
    FirebaseDatabase firebaseDatabase;

    List<Post> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dr = firebaseDatabase.getReference();
        user = auth.getCurrentUser();
        reference = storage.getReference(user.getEmail());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        helper = new InstaDatabaseHelper(this);

        list = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            getAllPostsFromFirebase();
        } else {
            getAllPostsFromLocalDatabase();
        }
    }

    private void getAllPostsFromLocalDatabase() {
        list = helper.getAllPosts();
        recyclerView.setAdapter(new RecyclerViewAdapter(Feed.this, list));
    }

    private void getAllPostsFromFirebase() {
        DatabaseReference databaseReference = dr.child("Users").child(user.getUid()).child("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                recyclerView.setAdapter(new RecyclerViewAdapter(Feed.this, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addImage() {
        Intent intent = new Intent(this, AddImage.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.profile) {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        } else if (itemId == R.id.settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        } else if (itemId == R.id.logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}