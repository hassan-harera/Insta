package com.harera.mychats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview(showBackground = true)
internal fun MyChatsTopBarPreview() {
    MyChatsTopBar{

    }
}

@Composable
internal fun MyChatsTopBar(
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(5.dp),

        ) {

        Icon(
            tint = MaterialTheme.colors.primary,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .width(35.dp)
                .padding(5.dp)
                .clickable {
                    onBackClicked()
                },
        )

        Text(
            text = "Chats",
            color = MaterialTheme.colors.primary,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            modifier = Modifier.padding(8.dp),
        )
    }
}