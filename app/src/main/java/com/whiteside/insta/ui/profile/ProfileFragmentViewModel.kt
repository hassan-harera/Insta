package com.whiteside.insta.ui.profile

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.adapter.PostsRecyclerViewAdapter
import com.whiteside.insta.model.Post
import com.whiteside.insta.model.Profile
import java.util.*

class ProfileFragmentViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var profile: MutableLiveData<Profile?> = MutableLiveData()
    var errorOccurred: MutableLiveData<Boolean> = MutableLiveData()
    var emptyPosts: MutableLiveData<Boolean> = MutableLiveData()

    var posts: MutableList<Post?> = ArrayList()
    var adapter = PostsRecyclerViewAdapter(posts)

    companion object {
        @JvmStatic
        @BindingAdapter("profile_posts")
        fun loadAdapter(view: RecyclerView, adapter: PostsRecyclerViewAdapter) {
            view.adapter = adapter
        }
    }

    fun loadProfile() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                profile.value = it?.toObject(Profile::class.java)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun loadProfilePosts() {
        fStore.collection("Users")
            .document(auth.uid ?: "")
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
}