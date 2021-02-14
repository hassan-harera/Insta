package com.whiteside.insta

import Model.Profile
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
import com.whiteside.insta.databinding.ActivityLoginBinding
import java.io.ByteArrayOutputStream
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val callbackManager = CallbackManager.Factory.create()
    private var facebookLogin: LoginButton? = null
    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var viewModel: LoginViewModel? = null
    private var bind: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        facebookLogin = bind!!.facebookLogin
        auth = FirebaseAuth.getInstance()
        registerFacebookCallback()
        setGoogleLoginListener()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth!!.signInWithCredential(credential)
                        .addOnFailureListener { e: Exception ->
                            e.printStackTrace()
                            com.whiteside.insta.GoogleSignIn.getGoogleSignInClient(this).signOut()
                        }
                        .addOnSuccessListener { authResult: AuthResult? -> checkUserInDatabase() }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun registerFacebookCallback() {
        facebookLogin!!.setPermissions(EMAIL, PUBLIC_PROFILE)
        facebookLogin!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                connectUserToFirebase(loginResult.accessToken)
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
                .addOnSuccessListener { authResult: AuthResult? -> checkUserInDatabase() }
                .addOnFailureListener { e: Exception ->
                    e.printStackTrace()
                    Toast.makeText(this@LoginActivity, "an error occurred", Toast.LENGTH_SHORT).show()
                    LoginManager.getInstance().logOut()
                }
    }

    private fun checkUserInDatabase() {
        viewModel!!.profileGetter.observe(this, { profile: Profile? ->
            if (profile != null) {
                successLogin()
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
        viewModel!!.profileSetter.observe(this, { profile1: Profile? -> successLogin() })
        viewModel!!.setProfile(profile, auth!!.uid)
    }

    private fun successLogin() {
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

    companion object {
        private const val EMAIL = "email"
        private const val PUBLIC_PROFILE = "public_profile"

        //To add feature for suggesting to add his photos
        private const val USER_PHOTOS = "user_photos"
        private const val GOOGLE_SIGN_IN = 1003
    }
}