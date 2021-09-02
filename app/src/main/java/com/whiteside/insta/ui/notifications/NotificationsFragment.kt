package com.whiteside.insta.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.whiteside.insta.ui.components.FollowRequestCard
import com.whiteside.insta.ui.components.LikeCard

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return ComposeView(requireContext())
    }
}

@Composable
@Preview
fun HomeNotifications() {
    LazyColumn {
        item {
            LikeCard()
        }
        item {
            FollowRequestCard()
        }
        item {
            LikeCard()
        }
        item {
            FollowRequestCard()
        }
        item {
            LikeCard()
        }
        item {
            FollowRequestCard()
        }
    }
}
