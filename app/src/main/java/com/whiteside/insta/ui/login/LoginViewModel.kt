package com.whiteside.insta.ui.login

import com.whiteside.insta.model.BlobBitmap
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso
import com.whiteside.insta.GoogleSignIn.getGoogleSignInClient
import com.whiteside.insta.model.Profile


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String = "LOGINVIEWMODEL"
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fStore = FirebaseFirestore.getInstance()
    var loginOperation: Boolean = false
    val profile = Profile()
    val operationCompleted = MutableLiveData<Boolean>()

    val googleSignInClient: GoogleSignInClient = getGoogleSignInClient(application)
    val callbackManager = CallbackManager.Factory.create()

    init {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        googleSignInClient.signOut()
    }

    // TODO Showing Toast from the viewmodel
    fun checkUserIsAlreadyRegistered(email: String) {
        FirebaseAuth
                .getInstance()
                .fetchSignInMethodsForEmail(email)
                .addOnSuccessListener {
                    loginOperation != it.signInMethods.isNullOrEmpty()
                }
    }

    fun onFacebookLogin(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun onGoogleLogin(data: Intent?) {
        val task = getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!

            getGoogleProfileInfo(account)
            checkUserIsAlreadyRegistered(account.email!!)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            authenticateToFirebase(credential)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }
    // TODO get Profile information
    fun getFacebookProfileInfo(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(loginResult.accessToken) { mProfile, response ->
            profile.name = response.jsonObject.getString("name")
            profile.bio = response.jsonObject.getString("name")
            profile.email = response.jsonObject.getString("email")

//            val picLink = Gson().fromJson(response.jsonObject.getString("profile_pic"), HashMap::class.java).get("url").toString()
//            val bitmap = LoadProfilePic().execute(Uri.parse(picLink)).get()
//            profile.profilePic = BlobBitmap.convertBitmapToBlob(bitmap)
//            val bitmap = LoadProfilePic().execute(Uri.parse("http://graph.facebook.com/${response.jsonObject.getString("id")}/picture")).get()
//            profile.profilePic = BlobBitmap.convertBitmapToBlob(bitmap)

            checkUserIsAlreadyRegistered(profile.email!!)
            val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
            authenticateToFirebase(credential)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    fun getGoogleProfileInfo(account: GoogleSignInAccount) {
        profile.email = account.email
        profile.name = account.displayName
        profile.bio = account.displayName

        val profilePic = LoadProfilePic().execute(account.photoUrl).get()
        profile.profilePic = BlobBitmap.convertBitmapToBlob(profilePic)
    }

    private fun addUserToDatabase() {
        fStore.collection("Users")
                .document(auth.uid!!)
                .set(profile, SetOptions.merge())
                .addOnSuccessListener {
                    goToFeed()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }


        //TODO Showing the image in the profile if his picture is null
//        val bitmap = BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.profile)
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
//        profile.profilePic = Blob.fromBytes(stream.toByteArray())

    }

    private fun connectUserToDatabase() {
        if (loginOperation) {
            goToFeed()
        } else {
            addUserToDatabase()
        }
    }

    private fun authenticateToFirebase(credential: AuthCredential) {
        auth.signOut()
        auth.signInWithCredential(credential)
                .addOnSuccessListener { connectUserToDatabase() }
                .addOnFailureListener { e: Exception ->
                    e.printStackTrace()
                    Toast.makeText(getApplication(), "an error occurred", Toast.LENGTH_SHORT).show()
                    LoginManager.getInstance().logOut()
                }
    }

    // TODO Finish Login Activity
    private fun goToFeed() {
        operationCompleted.value = true
    }

    companion object {
        //TODO Change deprecated Async Task
        class LoadProfilePic : AsyncTask<Uri, Void, Bitmap>() {
            override fun doInBackground(vararg params: Uri?): Bitmap {
                return Picasso.get().load(params[0]).get()
            }
        }

        class ProfilePic(val url: String)
    }
}