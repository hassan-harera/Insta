package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewPost extends AppCompatActivity {

    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    StorageReference reference;

    String UID, postID;

    TextView caption, likesNumber;
    ImageView recImage;
    ProgressBar bar;

    ListView likes_list;

    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        names = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        UID = FirebaseAuth.getInstance().getUid();
        postID = bundle.getString("Post ID");

        caption = findViewById(R.id.caption);
        recImage = findViewById(R.id.post_image);
        likesNumber = findViewById(R.id.likes_number);
        bar = findViewById(R.id.progress_bar);
        likes_list = findViewById(R.id.likes_list);

        getPost();
    }

    @SuppressLint("SetTextI18n")
    private void getPost() {
        ConnectivityManager cm = (ConnectivityManager) getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            databaseReference.child("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            caption.setText(snapshot.child(UID).child("Posts").child(postID)
                                    .child("Caption").getValue(String.class));
                            likesNumber.setText(snapshot.child(UID).child("Posts").child(postID)
                                    .child("Likes").getChildrenCount() + " Love");

                            for (DataSnapshot s : snapshot.child(UID).child("Posts").child(postID)
                                    .child("Likes").getChildren()) {

                                names.add(snapshot.child(s.getKey()).child("Name").getValue(String.class));
                            }
                            likes_list.setAdapter(new ArrayAdapter<String>
                                    (ViewPost.this, R.layout.support_simple_spinner_dropdown_item, names));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

            final long resolution = 1024 * 1024;
            reference.child("Users").child(UID).child("Posts").child(postID).
                    getBytes(resolution).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    if (task.isSuccessful()) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                        recImage.setImageBitmap(bitmap);
                        bar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ViewPost.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ViewPost.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}