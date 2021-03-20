package com.whiteside.insta.adapter

import com.whiteside.insta.model.Profile
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R.*
import com.whiteside.insta.databinding.CardViewChatBinding

class FriendsRecyclerViewAdapter(var friends: ArrayList<Profile>) :
    RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layout.card_view_chat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setChat(friends[position])
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    inner class ViewHolder(val bind: CardViewChatBinding) : RecyclerView.ViewHolder(bind.root) {
        fun setChat(profile: Profile) {
            bind.profile = profile
        }
    }
}