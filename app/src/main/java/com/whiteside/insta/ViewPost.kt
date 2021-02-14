package com.whiteside.insta

import Model.Post
import Model.Profile
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ViewPost : AppCompatActivity() {
    private var fStore: FirebaseFirestore? = null
    private var postID: String? = null
    private var date: TextView? = null
    private var profileName: TextView? = null
    private var caption: TextView? = null
    private var love_number: TextView? = null
    private var love: ImageView? = null
    private var postImage: ImageView? = null
    private var profileImage: ImageView? = null
    var bar: ProgressBar? = null
    private var names: List<String>? = null
    private var listView: ListView? = null
    private var adapter: ArrayAdapter<*>? = null
    private val profile: Profile? = null
    private var UID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)
        fStore = FirebaseFirestore.getInstance()
        names = ArrayList()
        listView = findViewById(R.id.likes_list)
        adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, names!!)
        listView!!.adapter = adapter
        val bundle = intent.extras
        postID = bundle!!.getString("Post ID")
        UID = bundle.getString("UID")
        caption = findViewById(R.id.caption)
        postImage = findViewById(R.id.post_image)
        love_number = findViewById(R.id.love_number)
        bar = findViewById(R.id.progress_bar)
        profileImage = findViewById(R.id.profile_image)
        date = findViewById(R.id.date)
        profileName = findViewById(R.id.profile_name)
        love = findViewById(R.id.love)
        getPosts()
    }


    private fun getPosts() {
        fStore!!.collection("Users")
                .document(UID!!)
                .collection("Posts")
                .document(postID!!)
                .addSnapshotListener { ds, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, e.stackTrace.toString())
                    } else if (ds!!.exists()) {
                        val post = ds.toObject(Post::class.java)
                        postImage!!.setImageBitmap(BitmapFactory.decodeByteArray(post!!.postImage!!.toBytes(), 0, post.postImage!!.toBytes().size))
                        date!!.text = post.time!!.toDate().toString()
                        caption!!.text = post.caption
                        love_number!!.text = post.likes!!.size.toString()
                        love!!.setImageResource(if (post.liked!!) R.drawable.loved else R.drawable.love)
                        bar!!.visibility = View.GONE
                    }
                }
        fStore!!.collection("Users")
                .document(UID!!)
                .addSnapshotListener { ds, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, e.stackTrace.toString())
                    } else if (ds!!.exists()) {
                        val bytes = ds.getBlob("profilePic")!!.toBytes()
                        profileImage!!.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                        profileName!!.text = ds.getString("name")
                    }
                }
    }
}