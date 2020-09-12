package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Controller.InstaDatabaseHelper;
import Model.User;

public class EditProfile extends AppCompatActivity {


    InstaDatabaseHelper helper;

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

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        helper = new InstaDatabaseHelper(this);

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
        progressBar = findViewById(R.id.progress_bar);

        getInfo();
        email.setText(user.getEmail());

    }

    private void getInfo() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getProfilePic();
            getName();
            getBio();
        } else {
            User user;
            user = helper.getUser(auth.getCurrentUser().getUid());
            if (user.getProfilePic() != null) {
                profilePic.setImageBitmap(user.getProfilePic());
            }
            name.setText(user.getName());
            bio.setText(user.getBio());
        }
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

    public void editClicked(final View view) {
        edit.setEnabled(false);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("Users").child(user.getUid()).child("Name").setValue(name.getText().toString());
        databaseReference.child("Users").child(user.getUid()).child("Bio").setValue(bio.getText().toString());

        if (uri != null) {
            progressBar.setVisibility(View.VISIBLE);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, bos);
            reference.child("Users").child(user.getUid()).child("Profile Pic").putBytes(bos.toByteArray())
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            finish();
                        }
                    });
        } else {
            finish();
        }
    }

    public void addImageClicked(View view) {
        profilePic.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profilePic.setEnabled(true);
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