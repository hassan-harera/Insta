package com.whiteside.insta.ui.post

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import com.whiteside.insta.model.Post
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.whiteside.insta.R
import com.whiteside.insta.R.*
import com.whiteside.insta.databinding.ActivityPostBinding
import com.whiteside.insta.model.BlobBitmap
import com.whiteside.insta.ui.edit_profile.EditProfileViewModel

class PostActivity : AppCompatActivity() {
    private lateinit var bind: ActivityPostBinding
    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, layout.activity_post)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        bind.viewModel = viewModel
        bind.post = Post()

        addOnImageClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(data!!.data!!.path)

            bind.post?.postImage = (BlobBitmap.convertBitmapToBlob(imageBitmap))
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun addOnImageClick() {
        bind.changeImage.setOnClickListener {
            ImagePicker
                .with(this)
                .compress(120)
                .maxResultSize(512, 512)
                .start()
        }
    }
}
