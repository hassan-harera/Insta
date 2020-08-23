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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
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
        storageReference = storage.getReference().child(firebaseUser.getEmail());

    }

    public void addImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == ADD_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 29) {
                uri = data.getData();
                ContentResolver contentResolver = getContentResolver();

                try {
                    InputStream inputStream = contentResolver.openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {

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
            storageReference.child(id + "").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        databaseHelper.insertPost(post);
                        successedAdd();
                    }else {
                        failedAdd();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Add an image firstly", Toast.LENGTH_LONG).show();
        }
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