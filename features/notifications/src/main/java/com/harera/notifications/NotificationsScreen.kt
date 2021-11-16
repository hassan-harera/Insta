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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.harera.model.response.Notification
import com.harera.compose.CommentCard
import com.harera.compose.LikeCard
import com.harera.model.model.Like
import org.koin.androidx.compose.getViewModel

@ExperimentalCoilApi
@Composable
fun HomeNotifications(
    notificationsViewModel: NotificationsViewModel = getViewModel(),
) {
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    val scrollState = rememberScrollState()
    val state = notificationsViewModel.state

    LaunchedEffect(key1 = true) {
        notificationsViewModel.intent.send(NotificationIntent.GetNotifications)
    }

    when (state) {
        is NotificationState.NotificationsFetched -> {
            notifications = state.notifications
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        notifications.forEach {
            when (it.type) {

                1 -> {
                    LikeCard(likeNotification = it, onNotificationClicked = {})
                }

                2 -> {
                    CommentCard(commentNotification = it, onNotificationClicked = {})
                }

            }
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