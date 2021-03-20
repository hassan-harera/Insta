package com.whiteside.insta.ui.edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import com.github.dhaval2404.imagepicker.ImagePicker
import com.whiteside.insta.BR
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityEditProfileBinding
import com.whiteside.insta.model.BlobBitmap
import com.whiteside.insta.model.Profile

class EditProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var bind: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        bind.viewModel = viewModel

        viewModel.finishActivity.observe(this) { finish() }
        viewModel.profile.observe(this) { bind.profile = it }
        viewModel.loadProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(data!!.data!!.path)

            bind.profile?.let {
                it.profilePic = BlobBitmap.convertBitmapToBlob(imageBitmap)
                bind.profileImage.setImageBitmap(imageBitmap)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}