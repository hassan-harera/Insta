package com.whiteside.insta.ui.feed

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.whiteside.insta.R
import com.whiteside.insta.databinding.FragmentFeedBinding
import com.whiteside.insta.ui.post.PostActivity

class FeedFragment : Fragment() {
    private lateinit var bind: FragmentFeedBinding
    private lateinit var viewModel: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_feed, container, false)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        bind.viewModel = viewModel

        viewModel.profile.observe(this as LifecycleOwner) {
            viewModel.loadProfilePosts(it)
        }
        viewModel.loadProfile()

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddPostClicked()
    }

    private fun onAddPostClicked() {
        bind.changeImage.setOnClickListener {
            startActivity(
                Intent(activity, PostActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    bind.changeImage,
                    "changeImage"
                ).toBundle()
            )
            requireActivity().finish()
        }
    }
}