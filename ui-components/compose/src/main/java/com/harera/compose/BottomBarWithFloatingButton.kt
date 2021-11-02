package com.harera.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harera.base.theme.Grey200

@Preview
@Composable
fun BottomBar() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Grey200,
                contentColor = Color.Unspecified,
                elevation = 0.dp,
                cutoutShape = CircleShape
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = null,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.add_image),
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.fillMaxWidth(0.2f))

                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = null,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.fillMaxWidth(0.15f),
                onClick = { /*TODO*/ },
                shape = CircleShape,
                backgroundColor = Color.White,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
    ) {

    }
}