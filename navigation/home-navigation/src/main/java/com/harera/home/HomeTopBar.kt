package com.harera.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
internal fun HomeTopBar(
    onMessagesClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(5.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = "Insta",
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterStart),
                color = MaterialTheme.colors.primary
            )

            Row(
                Modifier
                    .align(alignment = Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(5.dp)
                        .clickable {
                            onSearchClicked()
                        },
                    tint = MaterialTheme.colors.primary,
                )

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    painter = painterResource(id = R.drawable.comment),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(5.dp)
                        .clickable {
                            onMessagesClicked()
                        },
                    tint = MaterialTheme.colors.primary,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    HomeTopBar(
        {}, {}
    )
}