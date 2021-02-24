package com.whiteside.insta.ui.add_image

import Model.Post
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.ActivityAddImageBinding
import java.io.ByteArrayOutputStream

class AddImage : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var bind: ActivityAddImageBinding? = null
    private var imageBitmap: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        bind = ActivityAddImageBinding.inflate(layoutInflater)
        bind!!.addImage.setOnClickListener { addImageClicked() }
        bind!!.add.setOnClickListener { addClicked() }

        return bind!!.root
    }

    private fun addImageClicked() {
        ImagePicker
                .with(this)
                .compress(120)
                .maxResultSize(512, 512)
                .start { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        val fileUri = data?.data
                        bind!!.addImage.setImageURI(fileUri)
                        imageBitmap = BitmapFactory.decodeFile(fileUri!!.path)
                        Toast.makeText(context, "${imageBitmap!!.byteCount}", Toast.LENGTH_SHORT).show()
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun addClicked() {
        if (imageBitmap != null) {
            bind!!.add.isEnabled = false
            bind!!.caption.isEnabled = false
            bind!!.addImage.isEnabled = false
        } else {
            Toast.makeText(requireContext(), "Add an image firstly", Toast.LENGTH_LONG).show()
            return
        }
        bind!!.progressBar.visibility = View.VISIBLE
        val post = Post(
                caption = bind!!.caption.text.toString(),
                time = Timestamp.now(),
                uId = auth.uid,
                Id = Timestamp.now().seconds.toString()
        )
        val stream = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        post.postImage = Blob.fromBytes(byteArray)


        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.currentUser!!.uid)
                .collection("Posts")
                .document(post.Id!!)
                .set(post)
                .addOnSuccessListener {
                    Toast.makeText(context, "The image added Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to upload the image", Toast.LENGTH_LONG).show()
                }
    }

    companion object {
        const val ADD_IMAGE_REQUEST = 1025
        const val TAKE_PICTURE = 2015
        const val PERMISSION_REQUEST_CODE = 123
    }
}