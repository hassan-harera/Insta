package com.harera.insta

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
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
import com.harera.base.datastore.LocalStore
import com.harera.base.state.BaseState
import com.harera.base.theme.InstaTheme
import com.harera.home.HomeActivity
import com.harera.login.LoginActivity
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen(
        mainViewModel: MainViewModel = getViewModel(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
            val progress by animateLottieCompositionAsState(composition)
            val token = mainViewModel.token

            LottieAnimation(composition, progress, Modifier.background(MaterialTheme.colors.background))

            Text(
                text = "Skip intro",
                modifier = Modifier
                    .padding(20.dp)
                    .clickable {
                        checkUser(token)
                    },
                color = MaterialTheme.colors.primary
            )

            if (progress >= 0.99) {
                checkUser(token)
                finish()
            }
        }
    }

    @ExperimentalAnimationApi
    private fun checkUser(token: String?) {
        if (token.isNullOrBlank()) {
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
        } else {
            startActivity(
                Intent(
                    this@MainActivity,
                    HomeActivity::class.java
                )
            )
        }
    }
}

class MainViewModel constructor(
    userPreferences: LocalStore,
) : BaseViewModel<BaseState>(userSharedPreferences = userPreferences)