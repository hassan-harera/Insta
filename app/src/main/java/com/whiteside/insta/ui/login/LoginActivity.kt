package com.whiteside.insta.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.whiteside.insta.R
import com.whiteside.insta.ui.wall.WallActivity
import com.whiteside.insta.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var bind: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        bind.vieWModel = viewModel

        onFacebookClicked()
        bind.signInButton.setOnClickListener { onGoogleLoginClicked() }
    }

    // TODO : get Google Profile INFO
    // TODO : convert Login operation to use MVP
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            viewModel.onGoogleLogin(data)
        } else {
            viewModel.onFacebookLogin(requestCode, resultCode, data)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.operationCompleted.observe(this) {
            val intent = Intent(this, WallActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun onGoogleLoginClicked() {
        val signInIntent = viewModel.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    fun onFacebookClicked() {
        bind.facebookLogin.setPermissions(EMAIL, PUBLIC_PROFILE)
        bind.facebookLogin.registerCallback(
            viewModel.callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    viewModel.getFacebookProfileInfo(loginResult)
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "Login canceled", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: FacebookException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@LoginActivity,
                        "An error occurred while login",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    //TODO : What is companion object from kotlin documents
    companion object {
        const val EMAIL = "email"
        const val PUBLIC_PROFILE = "public_profile"
        const val GOOGLE_SIGN_IN = 1003

        //TODO : To add feature for suggesting to add his photos
        //private const val USER_PHOTOS = "user_photos"
    }

}