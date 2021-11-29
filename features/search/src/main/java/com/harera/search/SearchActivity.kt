package com.harera.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.harera.base.theme.InstaTheme
import com.harera.model.model.User
import com.harera.model.response.SearchedPost
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InstaTheme {
                SearchActivityContent()
            }
        }
    }
}

@Composable
fun SearchActivityContent(
    searchViewModel: SearchViewModel = getViewModel(),
) {
    val state = searchViewModel.state
    var searchedText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var postList by remember { mutableStateOf<List<SearchedPost>>(emptyList()) }
    val profileList by remember { mutableStateOf<List<User>>(emptyList()) }

    when (state) {
        is SearchState.PostsSearchResult -> {
            postList = state.posts
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar {
                searchedText = it
                scope.launch {
                    searchViewModel.intent.send(SearchIntent.Search(searchedText))
                }
            }
        }
    ) {
        LazyColumn {
            postList.forEach {
                item {
                    SearchedPostCard(
                        searchedPost = it,
                        onProfileClicked = {

                        },
                        onPostClicked = {

                        }
                    )
                }
            }
        }

    }
}