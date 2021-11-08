package com.harera.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.harera.base.theme.Blue250
import com.harera.home.HomeActivity
import com.harera.login.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
class LoginActivity : AppCompatActivity() {

    private lateinit var bind: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginActivityContent()
        }
    }

    private fun handleFailure(exception: Exception?) {
        exception?.printStackTrace()
        Toast.makeText(baseContext, exception?.message, Toast.LENGTH_SHORT).show()
        bind.loading.visibility = View.GONE
    }

    private fun handleLoading() {
        bind.loading.visibility = View.VISIBLE
    }

    private fun goToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
@Preview
fun LoginActivityContent() {
    val verticalScroll = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(verticalScroll)
            .fillMaxSize()
    ) {
        val tabs: List<LoginTabs> = arrayListOf(
            LoginTabs.Login,
            LoginTabs.Signup,
        )

        val pagerState: PagerState = rememberPagerState()

        LoginHeaderContent()
        LoginTabs(tabs = tabs, pagerState = pagerState)
        LoginTabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@Composable
@Preview
fun LoginHeaderContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(Color.Black),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun LoginTabs(tabs: List<LoginTabs>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Unspecified,
        contentColor = Color.Unspecified,
    ) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                modifier = Modifier.background(Color.Unspecified),
                selectedContentColor = Color.Unspecified,
                unselectedContentColor = Color.Unspecified,
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
fun LoginTabsContent(tabs: List<LoginTabs>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        count = tabs.size,
        verticalAlignment = Alignment.Top,
    ) { page ->
        tabs[page].content()
    }
}


