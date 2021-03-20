package com.whiteside.insta.ui.view_post

import com.whiteside.insta.model.Date_Time
import android.app.Application
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.R
import com.whiteside.insta.model.Profile
import com.whiteside.insta.model.Post

class PostViewModel(application: Application) : AndroidViewModel(application) {
    var UID: String? = null
    var postID: String? = null

    var fStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val post: MutableLiveData<Post> = MutableLiveData<Post>()
    val profile: MutableLiveData<Profile> = MutableLiveData<Profile>()

    val names = ArrayList<String>()


    companion object {
        @JvmStatic
        @BindingAdapter("date")
        fun getPostDate(view: TextView, post: Post?) {
            if (post != null) {
                view.text = Date_Time.timeFromNow(post.time!!)
            }
        }

        @JvmStatic
        @BindingAdapter("likes_list")
        fun getLikes(view: ListView, likes: ArrayList<String>) {
            val adapter =
                ArrayAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, likes)
            view.adapter = adapter
        }
    }


    internal fun getPost() {
        fStore.collection("Users")
            .document(UID!!)
            .collection("Posts")
            .document(postID!!)
            .get()
            .addOnSuccessListener {
                post.value = it.toObject(Post::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    internal fun loadProfile() {
        fStore.collection("Users")
            .document(UID!!)
            .get()
            .addOnSuccessListener {
                profile.value = it.toObject(Profile::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}