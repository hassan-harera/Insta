package com.whiteside.insta.ui.notifications

import com.whiteside.insta.adapter.NotificationsRecyclerViewAdapter
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
import com.whiteside.insta.R.*
import com.whiteside.insta.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    private lateinit var bind: FragmentNotificationsBinding
    private lateinit var vieWModel: NotificationsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = DataBindingUtil.inflate(layoutInflater, layout.fragment_notifications, container, false)
        vieWModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        bind.viewModel = vieWModel

        vieWModel.getLikesNotifications()
        vieWModel.getFriendRequests()

        return bind.root
    }
}