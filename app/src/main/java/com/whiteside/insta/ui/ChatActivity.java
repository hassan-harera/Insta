package com.whiteside.insta.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.whiteside.insta.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Controller.MessagesRecyclerViewAdapter;
import Model.Message;
import Model.Profile;

import static android.content.ContentValues.TAG;

public class ChatActivity extends AppCompatActivity {

    private List<Message> messages;

    private String UID;
    private MessagesRecyclerViewAdapter adapter;

    private FirebaseAuth auth;
    private DatabaseReference dRef;
    private FirebaseFirestore fStore;
    private ActivityChatBinding bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        setSupportActionBar(bind.appBar);

        UID = getIntent().getExtras().getString("UID");

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        bind.recyclerView.setHasFixedSize(true);
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList();
        adapter = new MessagesRecyclerViewAdapter(messages, this);
        bind.recyclerView.setAdapter(adapter);


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
                            bind.profileImage.setImageBitmap(bitmap);
                            bind.name.setText(profile.getName());
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
                                bind.recyclerView.smoothScrollToPosition(messages.size());
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
        String messageText = bind.messageSend.getText().toString();
        if (!messageText.equals("")) {
            Message m = new Message();
            m.setFrom(auth.getUid());
            m.setTo(UID);
            m.setSeconds(Timestamp.now().getSeconds());
            m.setMessage(bind.messageSend.getText().toString());

            sendMessage(m);
        }
    }

    protected Boolean sendMessage(Message m) {
        DatabaseReference dbReferences = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(auth.getUid());
        dbReferences = dbReferences
                .child("Users")
                .child(uid)
                .child("Chats")
                .child(m.getTo())
                .push();

        dbReferences.setValue(m);

        dbReferences
                .child("Users")
                .child(m.getTo())
                .child("Chats")
                .child(uid)
                .push()
                .setValue(m);

        return true;
    }
}