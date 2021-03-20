package com.whiteside.insta.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.whiteside.insta.R
import com.whiteside.insta.databinding.FragmentViewProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var bind: FragmentViewProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_view_profile, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        bind.viewModel = viewModel

        observeProfile()

        return bind.root
    }

    private fun observeProfile() {
        viewModel.profile.observe(viewLifecycleOwner) {
            bind.profile = it
        }
        viewModel.loadProfile(FirebaseAuth.getInstance().uid)
        viewModel.loadProfilePosts(FirebaseAuth.getInstance().uid)
    }
}