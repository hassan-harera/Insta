package com.whiteside.insta.ui.search_resault

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivitySearchResultBinding

class SearchResult : AppCompatActivity() {

    private lateinit var bind : ActivitySearchResultBinding
    private lateinit var viewModel: ProfileSearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        viewModel = ViewModelProvider(this).get(ProfileSearchViewModel::class.java)

        viewModel.profileUID = intent.extras?.getString("search")

        observe()
    }

    private fun observe(){
        viewModel.profile.observe (this){
            bind.profile = it
        }
        viewModel.loadProfileResult()
    }
}