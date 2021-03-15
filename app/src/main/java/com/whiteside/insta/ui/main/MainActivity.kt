package com.whiteside.insta.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.whiteside.insta.R
import com.whiteside.insta.ui.wall.WallActivity
import com.whiteside.insta.ui.login.LoginActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewModel: MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initializeFirebase(this)
    }

    override fun onStart() {
        super.onStart()
        viewSplashPage()
    }

    private fun viewSplashPage() {
        Handler().postDelayed({
            checkLogin()
        }, 2000)
    }

    private fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            goFeed()
        } else {
            goLogin()
        }
    }

    private fun goFeed() {
        val intent = Intent(this, WallActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}