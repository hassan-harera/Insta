package com.harera.insta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Grey660
import com.harera.insta.R

@Composable
@Preview
private fun FormBottomBar() {
    Surface(
        elevation = 8.dp,
    ) {
        Row(
            Modifier
                .background(Grey660)
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(25.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_image),
                    contentDescription = "",
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(25.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.attachment),
                    contentDescription = ""
                )
            }
        }
    }
}