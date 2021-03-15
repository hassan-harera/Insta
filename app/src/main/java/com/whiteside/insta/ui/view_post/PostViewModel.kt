package com.whiteside.insta.ui.view_post

import com.whiteside.insta.model.BlobBitmap
import com.whiteside.insta.model.Date_Time
import android.app.Application
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.R
import com.whiteside.insta.model.Profile
import com.whiteside.insta.model.Post

class PostViewModel(application: Application) : AndroidViewModel(application) {
    var fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val post: MutableLiveData<Post> = MutableLiveData<Post>()
    val profile: MutableLiveData<Profile> = MutableLiveData<Profile>()
    val names = ArrayList<String>()

    //TODO What is internal kwy word in kotlin
    internal fun getPost(UID: String?, postID: String?) {
        fStore.collection("Users")
            .document(UID!!)
            .collection("Posts")
            .document(postID!!)
            .get()
            .addOnSuccessListener {
                val p = it.toObject(Post::class.java)
                post.value = p
            }
            .addOnFailureListener {
                it.printStackTrace()
            }

        fStore.collection("Users")
            .document(UID)
            .get()
            .addOnSuccessListener {
                profile.value!!.profilePic = it.getBlob("profilePic")
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("profile_image")
        fun getProfilePic(view: ImageView, UID: String?) {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID ?: "")
                .get()
                .addOnSuccessListener {
                    view.setImageBitmap(BlobBitmap.convertBlobToBitmap(it.getBlob("profilePic")))
                }
        }

        @JvmStatic
        @BindingAdapter("profile_name")
        fun getProfileName(view: ImageView, UID: String?) {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID ?: "")
                .get()
                .addOnSuccessListener {
                    view.setImageBitmap(BlobBitmap.convertBlobToBitmap(it.getBlob("profilePic")))
                }
        }

        @JvmStatic
        @BindingAdapter("date")
        fun getPostDate(view: TextView, post: Post?) {
            if (post != null) {
                view.text = Date_Time.timeFromNow(post.time!!)
            }
        }

//        @JvmStatic
//        @BindingAdapter("love")
//        fun getLove(view: ImageView, post: Post?) {
//            if (post != null && post.liked!!) {
//                view.setImageResource(R.drawable.loved)
//            } else {
//                view.setImageResource(R.drawable.love)
//            }
//        }

        @JvmStatic
        @BindingAdapter("progress_bar")
        fun progressBar(view: ProgressBar, post: Post?) {
            if (post != null) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
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
}