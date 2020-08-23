package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    EditText password, username;
    TextView register;
    Button login;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register_text);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dbRef = database.getReference();
        reference = storage.getReference();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            goFeed();
        }else {
            createUserSchema();
        }
    }

    private void goFeed() {
        Intent intent = new Intent(this, Feed.class);
        startActivity(intent);
        finish();
    }

    private void createUserSchema() {
        dbRef.child("User");
    }

    public void loginClicked(View view) {
        String password = this.password.getText().toString(),
                username = this.username.getText().toString();

        if (username.equals("")) {
            this.username.setError("Username is required");
        } else if (password.equals("")) {
            this.username.setError("Password is required");
        } else {
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        successedLogin();
                    } else {
                        failedLogin();
                    }
                }
            });
        }
    }

    private void successedLogin() {
        Intent intent = new Intent(this, Feed.class);
        startActivity(intent);
        finish();
    }

    private void failedLogin() {
        Toast.makeText(this, "username or password is not correct", Toast.LENGTH_LONG).show();
    }

    public void registerClicked(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }
}