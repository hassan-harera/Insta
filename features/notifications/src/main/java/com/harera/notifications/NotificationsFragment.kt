package com.harera.notifications

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harera.compose.LikeCard
import com.harera.model.modelget.Like

@Composable
fun HomeNotifications(
    notificationsViewModel: NotificationsViewModel = hiltViewModel()
) {
    val notifications by notificationsViewModel.likeNotifications.observeAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        notifications?.let {
            LikeNotifications(it)
        }
    }

}

@Composable
fun LikeNotifications(
    likeNotifications: List<Like>,

) {
    val expanded by remember { mutableStateOf<Boolean>(false) }

    Column {
        if (likeNotifications.isNotEmpty())
            Row {
                Spacer(
                    modifier = Modifier
                        .border(0.5.dp, Color.Black)
                        .height(1.dp)
                        .fillMaxWidth(0.1f)
                )

                Text(text = "Like Notifications")

                Spacer(
                    modifier = Modifier
                        .border(0.5.dp, Color.Black)
                        .height(1.dp)
                        .fillMaxWidth()
                )
            }

        for (i in likeNotifications.indices) {
            if (!expanded && i >= 2) {
                break
            }

            LikeCard(
                like = likeNotifications[i] as Like,
                onNotificationClicked = {

                }
            )
        }

        if (likeNotifications.size > 2)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
            ) {
                Icon(
                    imageVector =
                    if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                )
            }
    }
}

@Composable
@Preview
fun ExpandView() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
        )
    }
}