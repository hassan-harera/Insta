package com.example.insta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Controller.ChatRecyclerViewAdapter;
import Model.Message;

public class Chat extends AppCompatActivity {


    List<Message> messages;

    EditText message_send;
    ImageView send;

    QuickContactBadge badge;
    TextView name;


    String UID;
    RecyclerView recycler_view;
    ChatRecyclerViewAdapter adapter;

    FirebaseAuth auth;
    DatabaseReference dRef;
    StorageReference sRef;
//    private InstaDatabaseHelper databaseHelper;


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
        sRef = FirebaseStorage.getInstance().getReference();
        dRef = FirebaseDatabase.getInstance().getReference();
//        databaseHelper = new InstaDatabaseHelper(this);

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList();
        adapter = new ChatRecyclerViewAdapter(messages, this);
        recycler_view.setAdapter(adapter);

        getMessages();
        getInfo();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getMessages();
            }
        }, 5000, 500);
    }

    private void getMessages() {
        dRef.child("Users").child(auth.getUid()).child("Chats").child(UID).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int i = 1;
                        for (DataSnapshot s : snapshot.getChildren()) {
                            if (i > messages.size()) {
                                Message m = new Message();
                                m.setFrom(s.child("From").getValue(String.class));
                                m.setTo(s.child("To").getValue(String.class));
                                m.setMessage(s.child("Message").getValue(String.class));
                                m.setDate(s.child("Date").getValue(String.class));
                                messages.add(m);
                                adapter.update(messages);
                                recycler_view.smoothScrollToPosition(messages.size());
                            }
                            ++i;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getInfo() {
//        if (!databaseHelper.checkUser(UID)) {
//            final User user = new User();
//            user.setUid(UID);
//            sRef.child("Users").child(UID)
//                    .child("Profile Pic").getBytes(1024 * 1024).
//                    addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                        @Override
//                        public void onSuccess(byte[] bytes) {
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            badge.setImageBitmap(bitmap);
//                            user.setProfilePic(bitmap);
//
//                            dRef.child("Users").child(UID)
//                                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot ds) {
//                                            user.setName(ds.child("Name").getValue(String.class));
//                                            name.setText(user.getName());
//                                            databaseHelper.insertUser(user);
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                        }
//                                    });
//                        }
//                    });
//        } else {
//            User user = databaseHelper.getUser(UID);
//            badge.setImageBitmap(user.getProfilePic());
//            name.setText(user.getName());
//        }
    }

    public void sendClicked(View view) {
        if (!message_send.getText().toString().equals("")) {
            DatabaseReference dbr = dRef.child("Users").child(auth.getUid()).child("Chats").child(UID).push();
            dbr.child("From").setValue(auth.getUid());
            dbr.child("To").setValue(UID);
            dbr.child("Date").setValue(new Date().toString());
            dbr.child("Message").setValue(message_send.getText().toString());

            dbr = dRef.child("Users").child(UID).child("Chats").child(auth.getUid()).push();
            dbr.child("From").setValue(auth.getUid());
            dbr.child("To").setValue(UID);
            dbr.child("Date").setValue(new Date().toString());
            dbr.child("Message").setValue(message_send.getText().toString());

            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager.isActive()) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            message_send.setText("");
        }
    }
}