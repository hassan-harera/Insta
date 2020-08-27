package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

public class EditProfile extends AppCompatActivity {

    RecyclerView view;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    StorageReference reference;
    FirebaseAuth auth;

    ImageView profilePic;
    EditText name, bio;
    TextView email;
    Button edit;
    Uri uri;
    Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        profilePic = findViewById(R.id.EditProfile_profile_Pic);
        edit = findViewById(R.id.EditProfile_edit);
        name = findViewById(R.id.EditProfile_name);
        bio = findViewById(R.id.EditProfile_bio);
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
                if (b.equals("")) {
                    bio.setText("Bio");
                } else {
                    bio.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getProfilePic() {
        reference.child("Users").child(user.getUid()).child("Profile Pic").getBytes((4096 * 4096)).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] b) {
                if (b != null) {
                    profilePic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                }
            }
        });
    }

    public void editClicked(View view) {
        databaseReference.child("Users").child(user.getUid()).child("Id").setValue(name.getText().toString());
        databaseReference.child("Users").child(user.getUid()).child("Bio").setValue(bio.getText().toString());

        if (uri != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bos);
            reference.child("Users").child(user.getUid()).child("Profile Pic").putBytes(bos.toByteArray());
        }
        finish();
    }

    public void addImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == 123 && resultCode == RESULT_OK) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap1 = Bitmap.createScaledBitmap(bitmap, 512, 512, true);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}