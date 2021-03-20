package com.whiteside.insta.ui.view_post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityViewPostBinding

class ViewPost : AppCompatActivity() {
    lateinit var bind: ActivityViewPostBinding
    lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_view_post)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        bind.viewModel = viewModel

        val bundle = intent.extras
        viewModel.postID = bundle!!.getString("postID")
        viewModel.UID = bundle.getString("UID")

        viewModel.post.observe(this) {
            bind.post = it
        }

        viewModel.profile.observe(this) {
            bind.profile = it
        }

        viewModel.getPost()
        viewModel.loadProfile()
    }
}