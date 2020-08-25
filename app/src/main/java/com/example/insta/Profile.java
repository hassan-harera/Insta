package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    RecyclerView view;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    StorageReference reference;
    FirebaseAuth auth;

    ImageView profilePic, edit;
    TextView name, bio, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        profilePic = findViewById(R.id.user_profile_photo);
        edit = findViewById(R.id.edit);
        name = findViewById(R.id.user_profile_name);
        bio = findViewById(R.id.user_profile_short_bio);
        email = findViewById(R.id.user_profile_email);

        getProfilePic();
        getName();
        getBio();
        email.setText(user.getEmail());
    }

    private void getName() {
        databaseReference.child("Users").child(user.getUid()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getBio() {
        databaseReference.child("Users").child(user.getUid()).child("Bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String b = snapshot.getValue(String.class);
                if(b.equals("")){
                    bio.setText("Bio-Here");
                }else {
                    bio.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getProfilePic() {
        reference.child("Users").child(user.getUid()).child("Profile Pic").getBytes((1024 * 1024)).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] b) {
                if (b != null) {
                    profilePic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                }
            }
        });
    }

    public void editClicked(View view) {
        Intent
    }
}