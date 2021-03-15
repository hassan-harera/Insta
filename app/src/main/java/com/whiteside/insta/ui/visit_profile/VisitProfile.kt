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

class VisitProfile : AppCompatActivity() {

    private lateinit var bind: ActivityVisitProfileBinding
    private lateinit var viewModel: VisitProfileViewModel
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_visit_profile)
        viewModel = ViewModelProvider(this).get(VisitProfileViewModel::class.java)

        bind.viewModel = viewModel

        recyclerView = bind.profilePosts
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = PostsRecyclerViewAdapter(viewModel.posts)

        viewModel.uID = intent.extras!!.getString("result")
        viewModel.loadProfile()
        viewModel.loadPosts()
    }

    override fun onResume() {
        super.onResume()
        viewModel.friendRequestSuccess.observe(this) {
            if (!it)
                Toast.makeText(this,resources.getString(R.string.add_friend_failed),Toast.LENGTH_SHORT).show()
        }
    }
}