package com.whiteside.insta.ui.edit_profile

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.ByteArrayOutputStream

class EditProfileViewModel(val activity: Activity, application: Application) : AndroidViewModel(application) {
    var profile = MutableLiveData<Profile>()

    companion object {
        @JvmStatic
        @BindingAdapter("profile_edit_img")
        fun getImage(view: ImageView, profile: Profile?) {
            if (profile!!.profilePic == null) {
                return
            }
            val bytes = profile.profilePic!!.toBytes()
            view.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }
    }

    fun getUser() {
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().uid!!)
                .get()
                .addOnSuccessListener { ds: DocumentSnapshot ->
                    profile.value = ds.toObject(Profile::class.java)
                }
    }

    //TODO Progress Bar
    fun onSaveClicked(view: View, profile: Profile) {
        val progressBar = ProgressBar(view.context)
        progressBar.visibility = View.VISIBLE
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().uid!!)
                .set(profile, SetOptions.merge())
                .addOnCompleteListener {
                    Toast.makeText(view.context, "Successfully Edited", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                    activity.finish()
                }
    }

    fun onChangeImageClicked(view: View, profile: Profile) {
        ImagePicker
                .with(activity)
                .compress(100)
                .maxResultSize(512, 512)
                .start { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        val imageBitmap = BitmapFactory.decodeFile(data!!.data!!.path)

                        val stream = ByteArrayOutputStream()
                        imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        profile.profilePic = Blob.fromBytes(byteArray)

                        (view as ImageView).setImageBitmap(imageBitmap)
                        Log.d("IMAGESIZE", "${byteArray.size}")
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(activity.baseContext, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity.baseContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}