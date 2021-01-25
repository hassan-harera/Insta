package com.whiteside.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText password, email;
    private Button login;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
    }

    public void loginClicked(View view) {
        final String password = this.password.getText().toString(),
                username = this.email.getText().toString();

        if (username.equals("")) {
            this.email.setError("E-mail is required");
        } else if (password.equals("")) {
            this.password.setError("Password is required");
        } else {
            login.setEnabled(false);
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        successLogin();
                    } else {
                        failedLogin();
                    }
                    login.setEnabled(true);
                }
            });
        }
    }

    private void successLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

    private void failedLogin() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            internetError();
        } else {
            Toast.makeText(this, "username or password is not correct", Toast.LENGTH_LONG).show();
        }
    }

    private void internetError() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Internet Problem");
        dialog.setMessage("There is an internet problem");

        dialog.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginClicked(login.getRootView());
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

    public void registerClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}