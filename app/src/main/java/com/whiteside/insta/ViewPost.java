package com.whiteside.insta;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import Model.Post;
import Model.Profile;

import static android.content.ContentValues.TAG;

public class ViewPost extends AppCompatActivity {

    private   FirebaseFirestore fStore;

    private     String postID;

    private     TextView date, profileName, caption, love_number;
    private     ImageView love, postImage, profileImage;
    public ProgressBar bar;


    private     List<String> names;
    private    ListView listView;
    private   ArrayAdapter adapter;
    private Post post;
    private Profile profile;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


         fStore = FirebaseFirestore.getInstance();

        names = new ArrayList<>();
        listView = findViewById(R.id.likes_list);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, names);
        listView.setAdapter(adapter);


        Bundle bundle = getIntent().getExtras();
        postID = bundle.getString("Post ID");
        UID = bundle.getString("UID");

        caption = findViewById(R.id.caption);
        postImage = findViewById(R.id.post_image);
        love_number = findViewById(R.id.love_number);
        bar = findViewById(R.id.progress_bar);
        profileImage = findViewById(R.id.profile_image);
        date = findViewById(R.id.date);
        profileName = findViewById(R.id.profile_name);
        love = findViewById(R.id.love);

        getPost();
    }


    @SuppressLint("SetTextI18n")
    private void getPost() {
        fStore.collection("Users")
                .document(UID)
                .collection("Posts")
                .document(postID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            Post post = ds.toObject(Post.class);
                            postImage.setImageBitmap(BitmapFactory.decodeByteArray(post.getPostImage()
                                    .toBytes(), 0, post.getPostImage().toBytes().length));
                            date.setText(post.getTime().toDate().toString());
                            caption.setText(post.getCaption());
                            love_number.setText(String.valueOf(post.getLikes().size()));
                            love.setImageResource(post.getLiked() ? R.drawable.loved : R.drawable.love);
                            bar.setVisibility(View.GONE);
                        }
                    }
                });

        fStore.collection("Users")
                .document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            byte[] bytes = ds.getBlob("profilePic").toBytes();
                            profileImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            profileName.setText(ds.getString("name"));
                        }
                    }
                });



    }
}
