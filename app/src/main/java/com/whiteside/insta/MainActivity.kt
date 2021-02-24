package com.whiteside.insta

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.whiteside.insta.databinding.ActivityMainBinding
import com.whiteside.insta.ui.login.LoginActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var bind: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind!!.root)
        SecretData.initializeFirebase(this)
        auth = FirebaseAuth.getInstance()

        try {
            val info = packageManager.getPackageInfo(
                    "com.whiteside.insta",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
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