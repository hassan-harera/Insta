package com.whiteside.insta.ui.login

import com.whiteside.insta.ui.edit_profile.Profile
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {
    var profileGetter = MutableLiveData<Profile?>()
    var profileSetter = MutableLiveData<Profile?>()
    fun getProfile(uid: String?) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid!!)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    profileGetter.value = documentSnapshot.toObject(Profile::class.java)
                }
    }

    fun setProfile(profile: Profile?, uid: String?) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid!!)
                .set(profile!!)
                .addOnSuccessListener { profileSetter.value = profile }
                .addOnFailureListener { e -> e.printStackTrace() }
    }
}