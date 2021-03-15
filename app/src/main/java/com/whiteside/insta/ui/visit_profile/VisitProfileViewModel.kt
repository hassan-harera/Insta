package com.whiteside.insta.ui.visit_profile

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.model.*
import kotlin.collections.ArrayList

class VisitProfileViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val friendRequestSuccess = MutableLiveData<Boolean>()
    var posts: ArrayList<Post> = ArrayList()
    var profile: Profile? = null
    var uID: String? = null
    var show: Boolean = false
    companion object {
        @JvmStatic
        @BindingAdapter("userProfilePicture")
        fun getProfilePicture(view: ImageView, profile: Profile?) {
            if (profile != null) {
                view.setImageBitmap(BlobBitmap.convertBlobToBitmap(profile.profilePic))
            }
        }

        @JvmStatic
        @BindingAdapter("add_friend")
        fun showAddFriend(view: LinearLayout, show: Boolean) {
            if (show) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
        }

        //TODO check if psosts added auto
        @JvmStatic
        @BindingAdapter("profile_posts")
        fun getPosts(view: RecyclerView, posts: ArrayList<Post>) {
            view.adapter!!.notifyDataSetChanged()
        }
    }

    fun loadProfile() {
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uID ?: "")
            .get()
            .addOnSuccessListener {
                profile = it.toObject(Profile::class.java)!!
                if (profile?.friends!!.contains(auth.uid!!)) {
                    show = false
                } else show = uID != auth.uid
            }
    }

    fun loadPosts() {
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uID!!)
            .collection("Posts")
            .orderBy("time")
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    posts.plus(it.toObject(Post::class.java))
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun addFriendClicked() {
        profile?.addFriendRequest(auth.uid!!)
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uID!!)
            .collection(Firebase.FRIEND_REQUESTS_COLLECTION)
            .document()
            .set(FriendRequestNotification(auth.uid!!, Timestamp.now()))
            .addOnSuccessListener {
                friendRequestSuccess.value = true
            }
            .addOnFailureListener {
                it.printStackTrace()
                friendRequestSuccess.value = false
            }
    }
}