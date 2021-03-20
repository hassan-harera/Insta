package com.whiteside.insta.ui.edit_profile

import com.whiteside.insta.model.Profile
import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    var profile = MutableLiveData<Profile?>()
    val finishActivity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun loadProfile() {
        FirebaseFirestore
            .getInstance()
            .collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                profile.value = it.toObject(Profile::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
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
                finishActivity.value = true
            }
    }

    fun onChangeImageClicked(view: View) {
        ImagePicker
            .with(view.context as Activity)
            .compress(100)
            .maxResultSize(512, 512)
            .start()
    }
}