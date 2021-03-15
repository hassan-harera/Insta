package com.whiteside.insta.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whiteside.insta.R
import com.whiteside.insta.databinding.FragmentViewProfileBinding
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter


class ProfileFragment : Fragment() {
    private lateinit var bind: FragmentViewProfileBinding
    private lateinit var viewModel: ProfileFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_view_profile, container, false)
        viewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        bind.viewModel = viewModel

        observeProfile()

        return bind.root
    }

    private fun observeProfile() {
        viewModel.profile.observe(viewLifecycleOwner) {
            bind.profile = it
        }
        viewModel.loadProfile()
        viewModel.loadProfilePosts()
    }
}