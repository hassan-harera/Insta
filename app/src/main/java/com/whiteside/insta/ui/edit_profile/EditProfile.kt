package com.whiteside.insta.ui.edit_profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityEditProfileBinding

class EditProfile : AppCompatActivity() {
    private lateinit var viewModel: EditProfileViewModel
    private var bind: ActivityEditProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = EditProfileViewModel(this, application)
        viewModel.profile.value = Profile()
        bind!!.viewModel = viewModel
        bind!!.profile = viewModel.profile.value

        getUser();
    }
    private fun getUser() {
        viewModel.profile.observe(this, Observer {
            bind!!.profile = it
        })
        viewModel.getUser()
    }
}