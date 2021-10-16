package com.harera.compose

import androidx.compose.foundation.background
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ButtonIconPre() {
    IconButton(
        onClick = {

        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.background(Color.Unspecified),
        )
//        modifier = Modifier.padding(12.dp),
    }
}