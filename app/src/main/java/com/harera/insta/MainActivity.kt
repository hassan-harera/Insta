package com.harera.insta

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseUser
import com.harera.repository.db.network.abstract_.AuthManager
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen()
        }
    }

    @Composable
    fun SplashScreen(
        mainViewModel: MainViewModel = getViewModel()
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
        val progress by animateLottieCompositionAsState(composition)
        val user by mainViewModel.user
        val context = LocalContext.current

        if (user == null) {
            mainViewModel.signIn()
        }

        Box {
            LottieAnimation(
                composition,
                progress,
            )

            Text(
                text = "Skip intro",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable {
                        startActivity(Intent(context, HomeActivity::class.java))
                        finish()
                    }
            )

            if (progress >= 1f && user != null) {
                startActivity(Intent(context, HomeActivity::class.java))
                finish()
            }
        }
    }

    class MainViewModel constructor(
        private val authManager: AuthManager,
    ) : ViewModel() {
        var user = mutableStateOf<FirebaseUser?>(null)

        fun getCurrentUser() {
            user.value = authManager.getCurrentUser()
        }

        fun signIn() {
            authManager.signInWithEmailAndRandomPassword(
                "hassan.shaban.harera@gmail.com"
            )
                .addOnSuccessListener {
                    user.value = it.user
                }
        }
    }
}