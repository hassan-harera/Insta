package com.harera.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Grey660

@Preview
@Composable
fun TopAppBarWithTitle() {
    TopBar(searchField = false, setNavigationButton = true)
}


@Preview
@Composable
fun TopAppBarWithSearch() {
    TopBar(searchField = true, setNavigationButton = true)
}


@Composable
fun TopBar(
    searchField: Boolean,
    title: @Composable () -> Unit = {
        Text(text = "Insta")
    },
    navigation: @Composable () -> Unit = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = ""
            )
        }
    },
    setNavigationButton: Boolean,
    actions: @Composable (RowScope) -> Unit = { Row {} },
    searchWord: MutableState<String> = mutableStateOf(""),
) {
    var searchWordText by remember {
        searchWord
    }

    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.padding(0.dp),
        backgroundColor = Grey660,
        contentColor = Grey660,
        title =
        if (searchField) {
            {
                TextField(
                    value = searchWordText,
                    label = {
                        Text(
                            text = "Search Any Thing",
                            textAlign = TextAlign.End
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = Color.White,
                        trailingIconColor = Color.Black,
                        backgroundColor = Color.Unspecified
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            //TODO call search method
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    onValueChange = {
                        searchWordText = it
                    },
                )
            }
        } else {
            title
        },
        actions = actions,
        navigationIcon = if (setNavigationButton) navigation else null
    )
}

@Preview
@Composable
fun HomeTopBar() {
    TopBar(
        searchField = false,
        setNavigationButton = false,
        title = {
            Text(text = "Insta", color = Color.White)
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = null,
                modifier = Modifier
                    .clickable {}
                    .padding(8.dp),
                tint = Color.White
            )
        }
    )
}
