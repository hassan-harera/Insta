package com.whiteside.insta

import Controller.Image
import Model.Post
import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.ActivityAddImageBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddImage : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var bind: ActivityAddImageBinding? = null
    private var reducedBitmap: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        bind = ActivityAddImageBinding.inflate(layoutInflater)
        bind!!.addImage.setOnClickListener { addImageClicked() }
        bind!!.add.setOnClickListener { addClicked() }

        return bind!!.root
    }

    private fun verifyPermission() {
        if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, TAKE_PICTURE)
        } else {
            requireActivity().requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            verifyPermission()
        }
    }

    private fun addImageClicked() {
        bind!!.addImage.isEnabled = false
        AlertDialog.Builder(context)
                .setTitle("Select photo way")
                .setPositiveButton("Take a picture") { dialog: DialogInterface?, which: Int -> verifyPermission() }
                .setNegativeButton("Select a picture") { dialog: DialogInterface?, which: Int ->
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, ADD_IMAGE_REQUEST)
                }
                .setOnDismissListener { dialog: DialogInterface? -> bind!!.addImage.isEnabled = true }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bind!!.add.isEnabled = true
        bind!!.caption.isEnabled = true
        bind!!.addImage.isEnabled = true
        if (data != null && requestCode == ADD_IMAGE_REQUEST && resultCode == -1) {
            val uri = data.data
            if (uri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    reducedBitmap = Image.getReducedBitmap(bitmap, 512)
                    Log.d("Image Uploaded", reducedBitmap!!.byteCount.toString())
                    bind!!.addImage.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == TAKE_PICTURE && resultCode == -1) {
            val bitmap = data!!.extras!!["data"] as Bitmap?
            reducedBitmap = bitmap
            bind!!.addImage.setImageBitmap(bitmap)
        }
    }

    fun addClicked() {
        if (reducedBitmap != null) {
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
        reducedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        post.postImage = Blob.fromBytes(byteArray)
        Log.d("byteArray", byteArray.size.toString() + "")
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.currentUser!!.uid)
                .collection("Posts")
                .document(post.Id!!)
                .set(post)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "The image added Successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Failed to upload the image", Toast.LENGTH_LONG).show()
                    }
                    bind!!.caption.setText("")
                    bind!!.addImage.setImageResource(R.drawable.add_image)
                    bind!!.addImage.isEnabled = true
                    bind!!.add.isEnabled = true
                    reducedBitmap = null
                    bind!!.caption.isEnabled = true
                    bind!!.progressBar.visibility = View.GONE
                }
    }

    companion object {
        const val ADD_IMAGE_REQUEST = 1025
        const val TAKE_PICTURE = 2015
        const val PERMISSION_REQUEST_CODE = 123
    }
}