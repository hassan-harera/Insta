package com.whiteside.insta

import Controller.PostsRecyclerViewAdapter
import Model.Post
import Model.Profile
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.whiteside.insta.databinding.FragmentViewProfileBinding
import java.util.*

class ProfileActivity : Fragment() {
    private val UID: String?
    private val auth: FirebaseAuth
    private val posts: MutableMap<String?, Post?>
    private val fStore: FirebaseFirestore
    private var adapter: PostsRecyclerViewAdapter? = null
    private var list: MutableList<Post?>

    //    private ImageView profileImage;
    //    private TextView name, bio;
    private var profile: Profile? = null
    private var bind: FragmentViewProfileBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentViewProfileBinding.inflate(inflater)
        val recyclerView = bind!!.profilePosts
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(bind!!.root.context)
        adapter = PostsRecyclerViewAdapter(list, context)
        recyclerView.adapter = adapter
        info
        profilePostsFromFirebaseFireStore
        return bind!!.root
    }

    private val info: Unit
        private get() {
            fStore.collection("Users")
                    .document(UID!!)
                    .addSnapshotListener { ds: DocumentSnapshot?, e: FirebaseFirestoreException? ->
                        try {
                            profile = Profile()
                            profile = ds!!.toObject(Profile::class.java)
                            val bytes = profile!!.profilePic!!.toBytes()
                            bind!!.userProfilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                            bind!!.userProfileName.text = profile!!.name
                            bind!!.userProfileShortBio.text = profile!!.bio
                        } catch (e1: Exception) {
                            e1.printStackTrace()
                        }
                    }
        }
    private val profilePostsFromFirebaseFireStore: Unit
        private get() {
            fStore.collection("Users")
                    .document(UID!!)
                    .collection("Posts")
                    .addSnapshotListener { qs: QuerySnapshot?, e: FirebaseFirestoreException? ->
                        if (e != null) {
                            e.printStackTrace()
                        } else {
                            if (!qs!!.documents.isEmpty()) {
                                for (ds in qs.documents) {
                                    val p = ds.toObject(Post::class.java)
                                    p!!.liked = p.likes!!.containsKey(auth.uid!!)
                                    posts[p.Id] = p
                                    list = ArrayList()
                                    list.addAll(posts.values)
                                    list.sortBy {it!!.time}
                                    adapter!!.update(list)
                                }
                            } else {
                                Toast.makeText(context, "No Posts", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
        }

    init {
        posts = HashMap()
        list = ArrayList()
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        UID = auth.uid
    }
}