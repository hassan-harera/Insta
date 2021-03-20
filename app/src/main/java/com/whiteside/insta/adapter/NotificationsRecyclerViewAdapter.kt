package com.whiteside.insta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R
import com.whiteside.insta.databinding.CardViewFriendRequestBinding
import com.whiteside.insta.databinding.CardViewLikeBinding
import com.whiteside.insta.model.*
import com.whiteside.insta.view_model.NotificationViewModel

class NotificationsRecyclerViewAdapter(private val notifications: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            val likeHolder = LikeHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.card_view_like,
                    parent,
                    false
                )
            )
            likeHolder
        } else {
            FriendRequestHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.card_view_friend_request,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return notifications[position].type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (notifications[position].type == 1) {
            (holder as LikeHolder).setNotification(notifications[position])
        } else {
            (holder as FriendRequestHolder).setNotification(notifications[position])
        }
    }


    override fun getItemCount(): Int {
        return notifications.size
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun setNotification(notification: Notification) {

        }
    }

    inner class LikeHolder(val bind: CardViewLikeBinding) : ViewHolder(bind.root) {
        val viewModel = NotificationViewModel()

        init {
            bind.viewModel = viewModel
        }

        override fun setNotification(notification: Notification) {
            viewModel.setLikeNotification(notification)

            viewModel.post.observe(bind.root.context as LifecycleOwner) {
                bind.post = it
            }
            viewModel.loadPost()
        }
    }

    inner class FriendRequestHolder(val bind: CardViewFriendRequestBinding) :
        ViewHolder(bind.root) {
        val viewModel = NotificationViewModel()

        init {
            bind.viewModel = viewModel
        }

        override fun setNotification(notification: Notification) {
            viewModel.setRequestNotification(notification)

            viewModel.profile.observe(bind.root.context as LifecycleOwner) {
                bind.profile = it
            }
            viewModel.loadProfile()
        }
    }
}