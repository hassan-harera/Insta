package com.whiteside.insta.ui.feed

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
import com.whiteside.insta.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    private lateinit var bind: FragmentFeedBinding
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)

        val viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        val adapter = PostsRecyclerViewAdapter(viewModel.list, context)

        recyclerView = bind.posts
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapter

//        bind.profile = Profile()
        viewModel.getProfile()

        return bind.root
    }


}