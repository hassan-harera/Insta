package com.whiteside.insta

import Controller.PostsRecyclerViewAdapter
import Model.Post
import Model.Profile
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.ActivityVisitProfileBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VisitProfile : AppCompatActivity() {
    private var UID: String? = null
    private var auth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var recyclerView: RecyclerView? = null
    private var adapter: PostsRecyclerViewAdapter? = null
    private var posts: HashMap<String, Post> = HashMap()
    private var list: ArrayList<Post> = ArrayList()
    private var fStore: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private var profile: Profile? = Profile()
    private var bind: ActivityVisitProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityVisitProfileBinding.inflate(layoutInflater)
        setContentView(bind!!.root)

        adapter = PostsRecyclerViewAdapter(list, this)

        recyclerView = bind!!.profilePosts
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setAdapter(adapter)

        val bundle = intent.extras
        UID = bundle!!["UID"].toString()
        info

        profilePostsFromFirebaseFireStore
    }

    override fun onStart() {
        super.onStart()
    }

    private val info: Unit
        private get() {
            fStore!!.collection("Users")
                    .document(UID!!)
                    .get()
                    .addOnSuccessListener(this) { ds ->
                        profile = ds.toObject(Profile::class.java)
                        bind!!.userProfileName.text = profile!!.name
                        bind!!.userProfileShortBio.text = profile!!.bio
                        bind!!.userProfilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(
                                profile!!.profilePic!!.toBytes(), 0, profile!!.profilePic!!.toBytes().size))
                        if (profile!!.getFriends()!!.contains(auth!!.uid!!)) {
                            bind!!.addFriend.visibility = View.INVISIBLE
                        } else if (UID == auth!!.uid) {
                            bind!!.addFriend.visibility = View.INVISIBLE
                        } else {
                            bind!!.addFriend.visibility = View.VISIBLE
                        }
                    }
        }
    private val profilePostsFromFirebaseFireStore: Unit
        private get() {
            fStore!!.collection("Users")
                    .document(UID!!)
                    .collection("Posts")
                    .get()
                    .addOnCompleteListener(this) { task ->
                        if (!task.isSuccessful) {
                            Log.e(ContentValues.TAG, "Error happened while download posts")
                            Log.e(ContentValues.TAG, task.exception!!.stackTrace.toString())
                        } else {
                            if (!task.result!!.isEmpty) {
                                for (ds in task.result!!.documents) {
                                    val p = ds.toObject(Post::class.java)
                                    posts.put(p!!.Id!!,  p)
                                    list = ArrayList()
                                    list.addAll(posts!!.values)
                                    Collections.sort(list)
                                    adapter!!.update(list)
                                }
                            } else {
                                Toast.makeText(this@VisitProfile, "No Posts", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
        }

    fun addFriendClicked(view: View?) {
        profile!!.addFriendRequest(auth!!.uid!!)
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID!!)
                .update("friendRequests", profile!!.getFriendRequests())
                .addOnCompleteListener { }
    }
}