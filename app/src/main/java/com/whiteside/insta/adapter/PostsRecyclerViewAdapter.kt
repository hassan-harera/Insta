package com.whiteside.insta.adapter

import com.whiteside.insta.model.Post
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R
import com.whiteside.insta.databinding.CardViewPostBinding
import com.whiteside.insta.ui.post.PostViewModel

class PostsRecyclerViewAdapter(var posts: List<Post?>) :
    RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = DataBindingUtil.inflate<CardViewPostBinding>(
            LayoutInflater.from(parent.context),
            R.layout.card_view_post,
            parent,
            false
        )
        return ViewHolder(bind)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setPost(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(val bind: CardViewPostBinding) : RecyclerView.ViewHolder(bind.root) {
        val viewModel = PostViewModel()

        init {
            bind.viewModel = viewModel
        }

        fun setPost(post: Post?) {
            bind.post = post

            viewModel.profile.observeForever {
                bind.profile = it
            }
            viewModel.loadProfile(post?.UID)
        }
    }
}