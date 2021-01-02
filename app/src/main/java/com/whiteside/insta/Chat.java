package com.whiteside.insta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import Controller.MessagesRecyclerViewAdapter;
import Model.Message;
import Model.Profile;

import static android.content.ContentValues.TAG;

public class Chat extends AppCompatActivity {


    List<Message> messages;

    EditText message_send;
    ImageView send;

    QuickContactBadge badge;
    TextView name;


    String UID;
    RecyclerView recycler_view;
    MessagesRecyclerViewAdapter adapter;

    FirebaseAuth auth;
    DatabaseReference dRef;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        UID = getIntent().getExtras().getString("UID");


        send = findViewById(R.id.send);
        message_send = findViewById(R.id.message_send);
        recycler_view = findViewById(R.id.recycler_view);
        badge = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList();
        adapter = new MessagesRecyclerViewAdapter(messages, this);
        recycler_view.setAdapter(adapter);


        fStore.collection("Users")
                .document(UID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, e.getStackTrace().toString());
                        } else if (ds.exists()) {
                            Profile profile = ds.toObject(Profile.class);

                            Bitmap bitmap = BitmapFactory.decodeByteArray(profile.getProfilePic().toBytes()
                                    , 0, profile.getProfilePic().toBytes().length);
                            badge.setImageBitmap(bitmap);
                            name.setText(profile.getName());
                        }
                    }
                });

        dRef.child("Users")
                .child(auth.getUid())
                .child("Chats")
                .child(UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int c = 1;
                        for (DataSnapshot s : snapshot.getChildren()) {
                            if (c > messages.size()) {
                                Message m = s.getValue(Message.class);
                                messages.add(m);
                                adapter.notifyDataSetChanged();
                                recycler_view.smoothScrollToPosition(messages.size());
                            }
                            c++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void sendClicked(View view) {
        if (!message_send.getText().toString().equals("")) {
            DatabaseReference dbr = dRef.child("Users").child(auth.getUid()).child("Chats").child(UID).push();

            Message m = new Message();
            m.setFrom(auth.getUid());
            m.setTo(UID);
            m.setSeconds(Timestamp.now().getSeconds());
            m.setMessage(message_send.getText().toString());

            dbr.setValue(m);

            dbr = dRef.child("Users").child(UID).child("Chats").child(auth.getUid()).push();

            dbr.setValue(m);


            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager.isActive()) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            message_send.setText("");

        }
    }
}