package com.whiteside.insta.ui.profile

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter
import com.whiteside.insta.model.Post
import com.whiteside.insta.model.Profile
import java.util.*

class ProfileViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var profile: MutableLiveData<Profile?> = MutableLiveData()

    var errorOccurred: MutableLiveData<Boolean> = MutableLiveData()
    var addFriendOperation = MutableLiveData<Boolean>()
    var emptyPosts: MutableLiveData<Boolean> = MutableLiveData()

    var posts: MutableList<Post?> = ArrayList()
    var adapter = PostsRecyclerViewAdapter(posts)

    companion object {
        @JvmStatic
        @BindingAdapter("add_friend")
        fun adaptAddFriend(view: LinearLayout, profile: Profile?) {
            if (profile != null) {
                val currentUid = FirebaseAuth.getInstance().uid!!
                if (currentUid.equals(profile.uid!!) || profile.friends!!.contains(currentUid) || profile.friendRequests!!.containsKey(
                        currentUid
                    )
                )
                    view.visibility = View.INVISIBLE
                else
                    view.visibility = View.VISIBLE
            }
        }
    }

    fun loadProfile(uid: String?) {
        fStore.collection("Users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                profile.value = it?.toObject(Profile::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun loadProfilePosts(uid: String?) {
        fStore.collection("Users")
            .document(uid!!)
            .collection("Posts")
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    posts.add(it.toObject(Post::class.java))
                    adapter.notifyDataSetChanged()
                }
                if (it.documents.isEmpty()) {
                    emptyPosts.value = true
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun onAddFriendClicked(view: View, profile: Profile?) {
        profile?.let {
            it.addFriendRequest(auth.uid!!)

            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(it.uid!!)
                .set(it, SetOptions.merge())
                .addOnSuccessListener {
                    view.visibility = View.INVISIBLE
                    addFriendOperation.value = true
                }
                .addOnFailureListener {
                    view.visibility = View.VISIBLE
                }
        }
    }
}