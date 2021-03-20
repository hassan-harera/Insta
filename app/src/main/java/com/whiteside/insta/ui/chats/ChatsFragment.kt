package com.whiteside.insta.ui.chats

import com.whiteside.insta.adapter.FriendsRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.whiteside.insta.R
import com.whiteside.insta.databinding.FragmentChatsBinding
import com.whiteside.insta.ui.chat.ChatViewModel

class ChatsFragment : Fragment() {

    private lateinit var bind: FragmentChatsBinding

    private var adapter: FriendsRecyclerViewAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)

        val viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        bind.viewModel = viewModel

        viewModel.getFriends()

        return bind.root
    }

}