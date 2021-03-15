package com.whiteside.insta.ui.feed

import com.whiteside.insta.model.Post
import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter
import com.whiteside.insta.model.Profile
import java.util.ArrayList

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    var profile: MutableLiveData<Profile> = MutableLiveData()
    var posts = ArrayList<Post?>()

    companion object {
        @JvmStatic
        @BindingAdapter("posts")
        fun getFriendsPosts(view: RecyclerView, adapter: PostsRecyclerViewAdapter?) {
            view.adapter = adapter
        }
    }

    fun loadProfile() {
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(FirebaseAuth.getInstance().uid!!)
            .get()
            .addOnSuccessListener { ds ->
                profile.value = ds.toObject(Profile::class.java)
            }
            .addOnFailureListener{
                it.printStackTrace()
            }
    }

    fun loadProfilePosts() {
        profile.value?.friends?.forEach {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(it)
                .collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        posts.add(it.toObject(Post::class.java))
                    }
                }
        }
    }
}