package com.whiteside.insta.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whiteside.insta.R

@Composable
@Preview
fun  MenuButton() {
    Icon(
        painterResource(id = R.drawable.menu),
        contentDescription = null,
        modifier = Modifier
            .clickable {
            }
            .padding(6.dp),
        tint = Color.Black,
    )
}