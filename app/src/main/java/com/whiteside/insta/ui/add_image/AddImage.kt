package com.whiteside.insta.ui.add_image

import com.whiteside.insta.ui.feed.Post
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.whiteside.insta.databinding.ActivityAddImageBinding

class AddImage : Fragment() {
    private var bind: ActivityAddImageBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        bind = ActivityAddImageBinding.inflate(layoutInflater)

        bind!!.viewModel = ViewModelProvider(this).get(AddImageViewModel::class.java)
        bind!!.post = Post()

        return bind!!.root
    }
}