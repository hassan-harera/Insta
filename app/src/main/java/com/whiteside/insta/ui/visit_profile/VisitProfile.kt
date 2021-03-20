package com.whiteside.insta.ui.visit_profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityVisitProfileBinding
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter
import com.whiteside.insta.ui.profile.ProfileViewModel

class VisitProfile : AppCompatActivity() {

    private lateinit var bind: ActivityVisitProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_visit_profile)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        bind.viewModel = viewModel

        val uid = intent.getStringExtra("UID")

        viewModel.profile.observe(this){
            bind.profile = it
        }
        viewModel.loadProfile(uid)
        viewModel.loadProfilePosts(uid)

        viewModel.addFriendOperation.observe(this) {
            Toast.makeText(this, resources.getText(R.string.friend_request_sent), Toast.LENGTH_SHORT).show()
        }
    }
}