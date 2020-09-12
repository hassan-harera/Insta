package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Controller.InstaDatabaseHelper;
import Model.User;

public class Register extends AppCompatActivity {

    EditText password, email, repassword, name;
    Button register;

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference reference;
    FirebaseAuth auth;
    DatabaseReference dbRef;
    FirebaseUser user;
    private InstaDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_register);
        repassword = findViewById(R.id.confirm_password_register);
        helper = new InstaDatabaseHelper(this);
        register = findViewById(R.id.register_signup);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dbRef = firebaseDatabase.getReference();
        reference = storage.getReference();

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
        user = auth.getCurrentUser();
        DatabaseReference userRef = dbRef.child("Users").child(user.getUid());
        userRef.child("Name").setValue(name.getText().toString());
        userRef.child("Username").setValue(email.getText().toString());
        userRef.child("Bio").setValue("Bio");
        userRef.child("Posts").child("Count").setValue(1);
        userRef.child("Notifications").child("Count").setValue(1);

        Uri uri = Uri.parse("android.resource://com.example.insta/drawable/profile");
        reference.child("Users").child(user.getUid()).child("Profile Pic").putFile(uri);
        if(!helper.checkUser(user.getUid())){
            User user = new User();
            user.setName(name.getText().toString());
            user.setBio("Bio");
            user.setEmail(auth.getCurrentUser().getEmail());
            user.setUid(auth.getCurrentUser().getUid());
            helper.insertUser(user);
        }
    }

    private void failedRegister() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
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

    private void goLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void succeedRegister() {
        Toast.makeText(this, "successful register", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        auth.signOut();
        startActivity(intent);
        finish();
    }
}