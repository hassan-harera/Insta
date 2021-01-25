package com.whiteside.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Controller.Connection;
import Model.Profile;


public class RegisterActivity extends AppCompatActivity {

    EditText password, email, repassword, name;
    Button register;

    FirebaseAuth auth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_register);
        repassword = findViewById(R.id.confirm_password_register);
        fStore = FirebaseFirestore.getInstance();
        register = findViewById(R.id.register_signup);
        auth = FirebaseAuth.getInstance();

    }


    public void registerClicked(View view) {
        final String password = this.password.getText().toString(),
                username = this.email.getText().toString(),
                repassword = this.repassword.getText().toString(),
                name = this.name.getText().toString();

        if (name.equals("")) {
            this.name.setError("Name is required");
        } else if (username.equals("")) {
            this.email.setError("Email is required");
        } else if (password.equals("")) {
            this.password.setError("Password is required");
        } else if (repassword.equals("")) {
            this.repassword.setError("Reenter password");
        } else if (!password.equals(repassword)) {
            this.repassword.setError("must be identical");
        } else {
            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        addUserToDatabase();
                        succeedRegister();
                    } else {
                        failedRegister();
                    }
                }
            });
        }
    }


    private void addUserToDatabase() {
        Profile profile = new Profile();
        profile.setName(name.getText().toString());
        profile.setBio("Bio");
        profile.setEmail(auth.getCurrentUser().getEmail());
        profile.setFriends(new ArrayList());
        profile.setFriendRequests(new HashMap());

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);

        profile.setProfilePic(Blob.fromBytes(stream.toByteArray()));


        fStore.collection("Users")
                .document(auth.getUid())
                .set(profile);
    }

    private void failedRegister() {
        if (!Connection.isConnected(this)) {
            internetError();
        } else {
            accountIsRegisteredError();
        }
    }

    private void internetError() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Internet Problem");
        dialog.setMessage("There is an internet problem");

        dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerClicked(register.getRootView());
                dialog.cancel();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void accountIsRegisteredError() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Account is already registered");

        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                goLogin();
            }
        });

        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }

    private void succeedRegister() {
        Toast.makeText(this, "successful register", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginClicked(View view) {
        goLogin();
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}