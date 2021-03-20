package com.whiteside.insta.ui.chat

import com.whiteside.insta.adapter.MessagesRecyclerViewAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whiteside.insta.R.*
import com.whiteside.insta.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessagesRecyclerViewAdapter
    private lateinit var bind: ActivityChatBinding
    private lateinit var viewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val UID = intent.extras!!.getString("UID")!!

        bind = DataBindingUtil.setContentView(this, layout.activity_chat)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        viewModel.UID = UID
        getProfile()
        setSupportActionBar(bind.appBar)

        adapter = MessagesRecyclerViewAdapter(viewModel.messages.value!!, this)

        bind.messages.setHasFixedSize(true)
        bind.messages.layoutManager = LinearLayoutManager(this)
        bind.messages.adapter = adapter
        bind.message = viewModel.message
    }

    private fun getProfile() {
        viewModel.profile.observe(this, {
            bind.profile = it
        })
        viewModel.getProfile()
    }
}