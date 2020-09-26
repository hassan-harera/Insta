package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Controller.Image;
import Model.Profile;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    ImageView profileImage;
    EditText name, bio;
    TextView email;
    Button edit;

    ProgressBar progressBar;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        profileImage = findViewById(R.id.user_profile_photo);
        edit = findViewById(R.id.EditProfile_edit);
        name = findViewById(R.id.EditProfile_name);
        bio = findViewById(R.id.EditProfile_bio);
        email = findViewById(R.id.user_profile_email);
        progressBar = findViewById(R.id.progress_bar);

        getInfo();
        email.setText(auth.getCurrentUser().getEmail());

    }

    private void getInfo() {
        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        profile = new Profile();
                        profile = ds.toObject(Profile.class);
                        byte[] bytes = profile.getProfilePic().toBytes();
                        profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        name.setText(profile.getName());
                        bio.setText(profile.getBio());
                        email.setText(ds.getString("email"));
                    }
                });
    }

    public void editClicked(final View view) {
        progressBar.setVisibility(View.VISIBLE);


        profile.setName(name.getText().toString());
        profile.setBio(bio.getText().toString());

        fStore.collection("Users")
                .document(auth.getUid())
                .set(profile, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfile.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                        finish();                    }
                });
    }

    public void addImageClicked(View view) {
        profileImage.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileImage.setEnabled(true);
        if (data != null && requestCode == 123 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Image.getReducedBitmap(bitmap, 512).compress(Bitmap.CompressFormat.PNG, 50, stream);

                profile.setProfilePic(Blob.fromBytes(stream.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}