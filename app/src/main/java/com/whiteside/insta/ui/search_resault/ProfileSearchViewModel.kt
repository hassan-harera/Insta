package com.whiteside.insta.ui.search_resault

import android.os.Handler
import android.widget.QuickContactBadge
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.model.BlobBitmap
import com.whiteside.insta.model.Profile
import java.util.*
import kotlin.collections.ArrayList

class ProfileSearchViewModel : ViewModel() {
    var profile: MutableLiveData<Profile?> = MutableLiveData()
    var profileUID: String? = null
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore = FirebaseFirestore.getInstance()

    companion object {
        @BindingAdapter("profile_image")
        fun loadProfilePic(view: QuickContactBadge, profile: Profile) {
            view.setImageBitmap(BlobBitmap.convertBlobToBitmap(profile.profilePic))
        }
    }

    fun loadProfileResult() {
        FirebaseFirestore
            .getInstance()
            .collection("Users")
            .document(profileUID ?: "")
            .get()
            .addOnSuccessListener {
                profile.value = it.toObject(Profile::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun searchUser(name: String): List<String?> {
        val q: Queue<String?> = LinkedList()
        q.add(auth!!.uid)
        val visited: MutableMap<String?, Boolean> = HashMap()
        visited[auth!!.uid] = true
        val count = intArrayOf(1)
        val list: ArrayList<String> = ArrayList()
        while (!q.isEmpty() && count[0] < 5000) {
            val cur = q.poll()
            count[0]++
            val p = arrayOfNulls<Profile>(1)
            fStore!!.collection("Users")
                .document(cur!!)
                .get()
                .addOnSuccessListener { ds ->
                    p[0] = ds.toObject(Profile::class.java)
                    if (p[0]!!.name!!.contains(name)) {
                        list.add(cur)
                    }
                }
            Handler().postDelayed({
                for (user in p[0]!!.friends!!) {
                    if (!visited.containsKey(user)) {
                        q.add(user)
                        visited[user] = true
                    }
                }
            }, 500)
        }
        return list
    }

    private fun searchProfile(name: String) {
        fStore.collection("Users")
            .whereLessThanOrEqualTo("name", name)
            .get()
            .addOnSuccessListener { qs ->
//                    val result = ArrayList<String>()
                qs.documents.iterator().forEach {
//                    visit(it.toObject(Profile::class.java)?.name)
                }
            }
    }
}