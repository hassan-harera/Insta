package com.whiteside.insta

import Controller.PostsRecyclerViewAdapter
import Model.Post
import Model.Profile
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    private lateinit var bind: FragmentFeedBinding
    private var posts: HashMap<String?, Post?> = HashMap()
    private var recyclerView: RecyclerView? = null
    private var adapter: PostsRecyclerViewAdapter? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var profile: Profile? = null
    private var list: MutableList<Post?> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = FragmentFeedBinding.inflate(layoutInflater)

        adapter = PostsRecyclerViewAdapter(list, context)

        recyclerView = bind.posts
        recyclerView!!.setLayoutManager(LinearLayoutManager(activity))
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setAdapter(adapter)

        return bind.root
    }

    override fun onStart() {
        super.onStart()
        fStore.collection("Users")
                .document(auth.uid!!)
                .get()
                .addOnSuccessListener { ds ->
                    profile = ds.toObject(Profile::class.java)
                    if (profile != null) friendsPosts
                }
    }

    private val friendsPosts: Unit
        get() {
            for (UID in profile!!.getFriends()!!) {
                fStore.collection("Users")
                        .document(UID)
                        .collection("Posts")
                        .get()
                        .addOnSuccessListener { qs ->
                            for (ds in qs.documents) {
                                val p = ds.toObject(Post::class.java)
                                p!!.liked = p.likes!!.containsKey(auth.uid!!)
                                posts[p.Id] = p
                                list = ArrayList()
                                list.addAll(posts.values)
                                list.sortBy { it!!.time }
                                adapter!!.update(list)
                            }
                        }
            }
        }
}