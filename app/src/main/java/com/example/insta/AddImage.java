package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import Controller.InstaDatabaseHelper;
import Model.Post;

public class AddImage extends AppCompatActivity {

    public static final int ADD_IMAGE_REQUEST = 1025;

    ImageView image;
    EditText title, message;
    Button add;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Uri uri;
    String id;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        image = findViewById(R.id.image_add);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        add = findViewById(R.id.add_add_image);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();
        storageReference = storage.getReference().child("Users").child(firebaseUser.getUid());

    }

    public void addImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        if (uri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addClicked(View view) {
        final InstaDatabaseHelper databaseHelper = new InstaDatabaseHelper(this);

        final Post post = new Post();
        int id = (int) new Date().getTime();
        post.setName(title.getText().toString());
        post.setDetails(this.message.getText().toString());
        post.setId(id);


        if (uri != null) {
            storageReference.child("Posts").child(id + "").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        if(databaseHelper.insertPost(post)) {
                            insertPostToFirebase(post);
                        }
                        successedAdd();
                    } else {
                        failedAdd();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Add an image firstly", Toast.LENGTH_LONG).show();
        }
    }

    private void insertPostToFirebase(Post post) {
        DatabaseReference dr = databaseReference.child("Users").child(firebaseUser.getUid()).child("Posts").child(post.getId() + "");
        dr.child("Id").setValue(post.getId());
        dr.child("Title").setValue(post.getName());
        dr.child("Details").setValue(post.getDetails());
    }

    private void failedAdd() {
        Toast.makeText(this, "Failed to upload the image", Toast.LENGTH_LONG).show();
    }

    private void successedAdd() {
        Toast.makeText(this, "The image added Successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Feed.class);
        startActivity(intent);
        finish();
    }
}