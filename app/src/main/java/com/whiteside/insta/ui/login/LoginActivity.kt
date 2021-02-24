package com.whiteside.insta.ui.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import com.whiteside.insta.R
import com.whiteside.insta.WallActivity
import com.whiteside.insta.databinding.ActivityLoginBinding
import com.whiteside.insta.ui.edit_profile.Profile
import java.io.ByteArrayOutputStream
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val callbackManager = CallbackManager.Factory.create()
    private var facebookLogin: LoginButton? = null
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var viewModel: LoginViewModel? = null
    private var bind: ActivityLoginBinding? = null
    private var profile: Profile = Profile()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        facebookLogin = bind!!.facebookLogin
        auth = FirebaseAuth.getInstance()

        registerFacebookCallback()
        setGoogleLoginListener()
    }

    // TODO : get Google Profile INFO
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
//                 val personName = account.displayName
//                /*  personPhotoUrl = account.getPhotoUrl().toString();
//                  email = account.getEmail();
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth!!.signInWithCredential(credential)
                        .addOnFailureListener { e: Exception ->
                            e.printStackTrace()
                            com.whiteside.insta.GoogleSignIn.getGoogleSignInClient(this).signOut()
                        }
                        .addOnSuccessListener { checkUserInDatabase() }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //TODO: move getting user info to another method
    private fun registerFacebookCallback() {
        facebookLogin!!.setPermissions(EMAIL, PUBLIC_PROFILE)
        facebookLogin!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(loginResult.accessToken) { pObject, response ->
                    profile.name = pObject.get("name") as String
                    profile.email = pObject.get("email") as String
                    profile.bio = pObject.get("bio") as String
                    val id: String = pObject.get("id") as String
                    val bitmap = Picasso.get().load("https://graph.facebook.com/$id/picture?type=large").get()

                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    profile.profilePic = Blob.fromBytes(stream.toByteArray())

                    Log.d("", response.toString())
                    Log.d("", pObject.toString())
                }
                val parameters = Bundle();
                parameters.putString("fields", "id,name,email,picture,bio");
                request.parameters = parameters;
                request.executeAsync();
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: FacebookException) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "An error occurred while login", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun connectUserToFirebase(accessToken: AccessToken) {
        auth!!.signOut()
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth!!.signInWithCredential(credential)
                .addOnSuccessListener { checkUserInDatabase() }
                .addOnFailureListener { e: Exception ->
                    e.printStackTrace()
                    Toast.makeText(this@LoginActivity, "an error occurred", Toast.LENGTH_SHORT).show()
                    LoginManager.getInstance().logOut()
                }
    }

    private fun checkUserInDatabase() {
        viewModel!!.profileGetter.observe(this, { profile: Profile? ->
            if (profile != null) {
                goToFeed()
            } else {
                addUserToDatabase()
            }
        })
        viewModel!!.getProfile(auth!!.uid)
    }

    private fun addUserToDatabase() {
        val profile = Profile()
        profile.name = Objects.requireNonNull(auth!!.currentUser)!!.displayName
        profile.bio = "Bio"
        profile.email = auth!!.currentUser!!.email
        profile.setFriends(ArrayList())
        profile.setFriendRequests(HashMap())
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.profile)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
        profile.profilePic = Blob.fromBytes(stream.toByteArray())
        viewModel!!.profileSetter.observe(this, { profile1: Profile? -> goToFeed() })
        viewModel!!.setProfile(profile, auth!!.uid)
    }

    private fun goToFeed() {
        val intent = Intent(this@LoginActivity, WallActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setGoogleLoginListener() {
        googleSignInClient = com.whiteside.insta.GoogleSignIn.getGoogleSignInClient(this)
        bind!!.signInButton.setOnClickListener { v: View? ->
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }
    }

    //TODO : What is companion object from kotlin documents
    companion object {
        private const val EMAIL = "email"
        private const val PUBLIC_PROFILE = "public_profile"

        //TODO : To add feature for suggesting to add his photos
        private const val USER_PHOTOS = "user_photos"
        private const val GOOGLE_SIGN_IN = 1003
    }
}