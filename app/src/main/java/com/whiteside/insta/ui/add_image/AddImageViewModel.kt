package com.whiteside.insta.ui.add_image

import com.whiteside.insta.ui.feed.Post
import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AddImageViewModel(application: Application) : AndroidViewModel(application) {

    fun addImageClicked(view: View, post: Post) {
        ImagePicker
                .with(view.context as Activity)
                .compress(120)
                .maxResultSize(512, 512)
                .start { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        val fileUri = data?.data
                        (view as ImageView).setImageURI(fileUri)

                        val imageBitmap = BitmapFactory.decodeFile(fileUri!!.path)
                        val stream = ByteArrayOutputStream()
                        imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        post.postImage = Blob.fromBytes(byteArray)

                        Toast.makeText(view.context, "${imageBitmap.byteCount}", Toast.LENGTH_SHORT).show()
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(view.context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(view.context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    fun addClicked(view: View, post: Post) {
        if (post.postImage != null) {
            view.isEnabled = false
        } else {
            Toast.makeText(view.context, "Add an image firstly", Toast.LENGTH_LONG).show()
            return
        }

        //TODO How to show progress bar on a context
        val auth = FirebaseAuth.getInstance()

        post.time = Timestamp.now()
        post.uId = auth.uid
        post.Id = Timestamp.now().seconds.toString()

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.currentUser!!.uid)
                .collection("Posts")
                .document(post.Id!!)
                .set(post)
                .addOnSuccessListener {
                    Toast.makeText(view.context, "The image added Successfully", Toast.LENGTH_LONG).show()
                    (view.context as Activity).startActivity((view.context as Activity).intent)
                    (view.context as Activity).finish()
                }
                .addOnFailureListener {
                    Toast.makeText(view.context, "Failed to upload the image", Toast.LENGTH_LONG).show()
                }
    }
}