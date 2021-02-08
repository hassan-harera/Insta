package com.whiteside.insta.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.Blob;
import com.whiteside.insta.R;
import com.whiteside.insta.WallActivity;
import com.whiteside.insta.databinding.ActivityLoginBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import Model.Profile;

import static com.whiteside.insta.ui.GoogleSignIn.getGoogleSignInClient;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    //To add feature for suggesting to add his photos
    private static final String USER_PHOTOS = "user_photos";

    private static final int GOOGLE_SIGN_IN = 1003;

    private final CallbackManager callbackManager = CallbackManager.Factory.create();
    private LoginButton facebookLogin;

    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    private LoginViewModel viewModel;
    private ActivityLoginBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        facebookLogin = bind.facebookLogin;
        auth = FirebaseAuth.getInstance();

        registerFacebookCallback();
        setGoogleLoginListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            getGoogleSignInClient(this).signOut();
                        })
                        .addOnSuccessListener(authResult -> checkUserInDatabase());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void registerFacebookCallback() {
        facebookLogin.setPermissions(EMAIL, PUBLIC_PROFILE);

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                connectUserToFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "An error occurred while login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectUserToFirebase(AccessToken accessToken) {
        auth.signOut();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> checkUserInDatabase())
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "an error occurred", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                });
    }

    private void checkUserInDatabase() {
        viewModel.profileGetter.observe(this, profile -> {
            if (profile != null) {
                successLogin();
            } else {
                addUserToDatabase();
            }
        });
        viewModel.getProfile(auth.getUid());
    }

    private void addUserToDatabase() {
        Profile profile = new Profile();
        profile.setName(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
        profile.setBio("Bio");
        profile.setEmail(auth.getCurrentUser().getEmail());
        profile.setFriends(new ArrayList<>());
        profile.setFriendRequests(new HashMap<>());


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);

        profile.setProfilePic(Blob.fromBytes(stream.toByteArray()));

        viewModel.profileSetter.observe(this, profile1 -> successLogin());
        viewModel.setProfile(profile, auth.getUid());
    }


    private void successLogin() {
        Intent intent = new Intent(LoginActivity.this, WallActivity.class);
        startActivity(intent);
        finish();
    }

    public void setGoogleLoginListener() {
        googleSignInClient = getGoogleSignInClient(this);
        bind.signInButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        });
    }
}