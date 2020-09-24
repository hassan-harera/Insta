package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Controller.Image;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    ImageView profileImage;
    EditText name, bio;
    TextView email;
    Button edit;
    Uri uri;

    ProgressBar progressBar;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        profileImage = findViewById(R.id.profile_image);
        edit = findViewById(R.id.EditProfile_edit);
        name = findViewById(R.id.EditProfile_name);
        bio = findViewById(R.id.EditProfile_bio);
        email = findViewById(R.id.user_profile_email);
        progressBar = findViewById(R.id.progress_bar);

        getInfo();
        email.setText(auth.getCurrentUser().getEmail());

    }

    private void getInfo() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        fStore.collection("Users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds) {
                        byte[] bytes = ds.getBlob("Profile Pic").toBytes();
                        profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        name.setText(ds.getString("Name"));
                        bio.setText(ds.getString("Bio"));
                        email.setText(ds.getString("Email"));
                    }
                });
    }

    public void editClicked(final View view) {
        Map<String, Object> map = new HashMap();
        map.put("Name", name.getText().toString());
        map.put("Bio", bio.getText().toString());

        fStore.collection("Users")
                .document(auth.getUid())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "Successfully Edited", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Cannot edit", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        if (uri != null) {
            progressBar.setVisibility(View.VISIBLE);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Image.getReducedBitmap(bitmap, 512).compress(Bitmap.CompressFormat.PNG, 50, bos);
            fStore.collection("Users")
                    .document(auth.getUid())
                    .update("Profile Pic", Blob.fromBytes(bos.toByteArray()));
        } else {
            finish();
        }
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
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}