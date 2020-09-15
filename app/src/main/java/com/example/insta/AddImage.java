package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigator;
import androidx.viewpager2.widget.ViewPager2;

import android.app.MediaRouteButton;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import Controller.InstaDatabaseHelper;
import Model.Post;

public class AddImage extends Fragment {

    public static final int ADD_IMAGE_REQUEST = 1025;

    ImageView image;
    EditText caption;
    Button add;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Bitmap bitmapImg;
    Uri uri;
    String id;

    private ProgressBar progressBar;
    private View view;


    public AddImage() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();
        storageReference = storage.getReference().child("Users").child(firebaseUser.getUid());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_image, container, false);

        image = view.findViewById(R.id.image_add);
        caption = view.findViewById(R.id.caption);
        add = view.findViewById(R.id.add_add_image);
        progressBar = view.findViewById(R.id.progress_bar);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageClicked(v);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClicked(v);
            }
        });

        return view;
    }

    public void addImageClicked(View view) {
        image.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        image.setEnabled(true);
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == ADD_IMAGE_REQUEST && resultCode == -1) {
            uri = data.getData();
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    bitmapImg = Bitmap.createScaledBitmap(bitmap, 600, 600,
                            true);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addClicked(View view) {
        if (bitmapImg != null) {
            image.setEnabled(false);
            add.setEnabled(false);
            caption.setEnabled(false);
        } else {
            Toast.makeText(view.getContext(), "Add an image firstly", Toast.LENGTH_LONG).show();
            return;
        }

        ViewPager2 v = getActivity().findViewById(R.id.view_pager);
        v.setEnabled(false);

        final Post post = new Post();
        databaseReference.child("Users").child(firebaseUser.getUid()).child("Posts").child("Count")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        post.setId(Integer.parseInt(snapshot.getValue().toString()));
                        databaseReference.child("Users").child(firebaseUser.getUid()).child("Posts")
                                .child("Count").setValue(post.getId() + 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        databaseReference.child("Users").child(firebaseUser.getUid()).child("Posts")
                .child(post.getId() + "").child("Likes");

        post.setCaption(caption.getText().toString());
        post.setDate(new Date().toString());
        post.setLikes(0);
        post.setLiked(false);
        post.setUID(firebaseAuth.getUid());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                add.setEnabled(false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                storageReference.child("Posts").child(post.getId() + "").putBytes(byteArray).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            insertPostToFirebase(post);
                            progressBar.setVisibility(View.GONE);
                            successAdd();
                        } else {
                            failedAdd();
                        }
                    }
                });
            }
        }, 1000);
    }

    private void insertPostToFirebase(Post post) {
        DatabaseReference dr = databaseReference.child("Users").child(firebaseUser.getUid()).child("Posts").child(post.getId() + "");
        dr.child("Id").setValue(post.getId());
        dr.child("Caption").setValue(post.getCaption());
        dr.child("Date").setValue(post.getDate());
        dr.child("Likes");
    }

    private void failedAdd() {
        Toast.makeText(getContext(), "Failed to upload the image", Toast.LENGTH_LONG).show();
    }

    private void successAdd() {
        Toast.makeText(getContext(), "The image added Successfully", Toast.LENGTH_LONG).show();
        ViewPager2 v = getActivity().findViewById(R.id.view_pager);
        v.setCurrentItem(2, true);


        caption.setText("");
        image.setImageResource(R.drawable.add_image);
        image.setEnabled(true);
        add.setEnabled(true);
        bitmapImg = null;
        caption.setEnabled(true);
    }
}