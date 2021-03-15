package com.whiteside.insta.ui.feed

import android.content.Intent
import com.whiteside.insta.model.Post
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter
import com.whiteside.insta.databinding.FragmentFeedBinding
import com.whiteside.insta.ui.edit_profile.EditProfileActivity
import com.whiteside.insta.ui.post.PostActivity

class FeedFragment : Fragment() {
    private lateinit var bind: FragmentFeedBinding
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        val viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        val adapter = PostsRecyclerViewAdapter(ArrayList<Post>())

        recyclerView = bind.posts
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapter

        viewModel.loadProfile()
        viewModel.loadProfilePosts()

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddPostClicked()
    }

    fun onAddPostClicked() {
        bind.addPost.setOnClickListener {
            startActivity(Intent(activity, PostActivity::class.java))
        }
    }
}