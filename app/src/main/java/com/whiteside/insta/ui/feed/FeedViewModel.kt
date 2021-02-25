package com.whiteside.insta.ui.feed

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.whiteside.insta.ui.edit_profile.Profile

class FeedViewModel(application: Application) : AndroidViewModel(application) {
    val posts: ArrayList<Post> = ArrayList()
    var profile: Profile? = null

    companion object {
        @JvmStatic
        @BindingAdapter("posts")
        fun getFriendsPosts(view: RecyclerView, profile: Profile?) {
            if (profile != null)
                for (UID in profile.getFriends()!!) {
                    FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(UID)
                            .collection("Posts")
                            .orderBy("time", Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener { qs ->
                                for (ds in qs.documents) {
                                    val p = ds.toObject(Post::class.java)
                                    p!!.liked = p.likes!!.containsKey(FirebaseAuth.getInstance().uid!!)
                                    (view.adapter as PostsRecyclerViewAdapter).addPost(p)
                                    view.adapter!!.notifyDataSetChanged()
                                }
                            }
                }
        }
    }


    fun getProfile() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().uid!!)
                .get()
                .addOnSuccessListener { ds ->
                    profile = ds.toObject(Profile::class.java)
                }
    }
}