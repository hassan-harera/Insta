package com.harera.insta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.UserSharedPreferences
import com.harera.base.state.State
import com.harera.home.HomeActivity
import com.harera.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel


@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen()
        }
    }

    @Composable
    fun SplashScreen(
        mainViewModel: MainViewModel = getViewModel(),
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
        val progress by animateLottieCompositionAsState(composition)
        val token = mainViewModel.token
        Log.d(TAG, "SplashScreen: $token")

        Box {
            LottieAnimation(composition, progress)

            Text(
                text = "Skip intro",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable {
                        checkUser(token)
                    }
            )

            if (progress >= 0.9) {
                checkUser(token)
            }
        }
    }

    private fun checkUser(token: String?) {
        if (token.isNullOrBlank()) {
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        } else {
            startActivity(
                Intent(
                    this@MainActivity,
                    HomeActivity::class.java
                )
            )
            finish()
        }
    }
}

class MainViewModel constructor(
    userPreferences: UserSharedPreferences,
) : BaseViewModel<State>(userSharedPreferences = userPreferences)