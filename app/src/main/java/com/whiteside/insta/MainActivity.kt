package com.whiteside.insta

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.whiteside.insta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var bind: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        SecretData.initializeFirebase(this)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        viewSplashPage()
    }

    private fun viewSplashPage() {
        Handler().postDelayed(Runnable { checkLogin() }, 2000)
    }

    private fun checkLogin() {
        if (auth!!.currentUser != null) {
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