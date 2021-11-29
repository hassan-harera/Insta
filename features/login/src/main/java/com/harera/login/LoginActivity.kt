package com.harera.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.harera.base.theme.InstaTheme
import com.harera.compose.FacebookRegisterButton
import com.harera.compose.GoogleRegisterButton
import com.harera.home.HomeActivity
import kotlinx.coroutines.launch
import org.checkerframework.common.value.qual.IntRangeFromGTENegativeOne
import org.koin.android.ext.android.inject
import java.util.*


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private val socialLoginViewModel: SocialLoginViewModel by inject()

    private val googleLoginLauncher = registerForActivityResult(GoogleLoginActivityResult()) {
        it ?: return@registerForActivityResult
        lifecycleScope.launch {
            socialLoginViewModel.intent.send(SocialLoginIntent.GoogleLogin(it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaTheme {
                val state = socialLoginViewModel.state

                when(state) {
                    is SocialLoginState.LoggedSuccessfully -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }

                LoginActivityContent()
            }
        }

        initializeFacebookLogin()
    }

    private fun initializeFacebookLogin() {
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application = application)
    }

    @Composable
    @Preview
    fun LoginActivityContent() {
        val verticalScroll = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .verticalScroll(verticalScroll)
        ) {
            val tabs: List<LoginTabs> = arrayListOf(
                LoginTabs.Login,
                LoginTabs.Signup,
            )

            val pagerState: PagerState = rememberPagerState()

            LoginHeaderContent()
            LoginTabs(tabs = tabs, pagerState = pagerState)
            LoginTabsContent(tabs = tabs, pagerState = pagerState)

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FacebookRegisterButton {
                    facebookLogin()
                }

                Spacer(modifier = Modifier.width(20.dp))

                GoogleRegisterButton {
                    googleLoginLauncher.launch(Unit)
                }
            }
        }
    }

    private fun facebookLogin() {
        val callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {
                    error.printStackTrace()
                    Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: LoginResult?) {
                    lifecycleScope.launch {
                        result?.let {
                            socialLoginViewModel
                                .intent
                                .send(SocialLoginIntent.FacebookLogin(result.accessToken.token))
                        }
                    }
                }
            })

        LoginManager
            .getInstance()
            .logIn(
                this@LoginActivity,
                callbackManager,
                listOf("email")
            )
    }

    @Composable
    @Preview
    private fun LoginHeaderContent() {
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth()
        ) {
            Text(
                text = "Insta",
                fontFamily = FontFamily.Cursive,
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
                fontSize = 48.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    @Composable
    private fun LoginTabs(tabs: List<LoginTabs>, pagerState: PagerState) {
        val scope = rememberCoroutineScope()

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary,
        ) {
            tabs.forEachIndexed { index, tab ->
                LeadingIconTab(
                    modifier = Modifier.background(Color.Unspecified),
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.primary,
                    text = {
                        Text(text = tab.title)
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    icon = {

                    }
                )
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    private fun LoginTabsContent(tabs: List<LoginTabs>, pagerState: PagerState) {
        HorizontalPager(
            state = pagerState,
            count = tabs.size,
            verticalAlignment = Alignment.Top,
        ) { page ->
            tabs[page].content()
        }
    }
}