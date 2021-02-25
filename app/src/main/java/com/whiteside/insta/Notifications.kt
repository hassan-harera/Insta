package com.whiteside.insta

import Controller.NotificationsRecyclerViewAdapter
import Model.Notifications.FriendRequestNotification
import Model.Notifications.LikeNotification
import Model.Notifications.Notification
import com.whiteside.insta.ui.feed.Post
import com.whiteside.insta.ui.edit_profile.Profile
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.FragmentNotificationsBinding
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Notifications : Fragment() {
    private lateinit var bind: FragmentNotificationsBinding
    private val notifications: HashMap<String, Notification?> = HashMap()
    private var list: ArrayList<Notification?> = ArrayList()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var recyclerView: RecyclerView? = null
    private var adapter: NotificationsRecyclerViewAdapter? = null
    private var profile: Profile? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = FragmentNotificationsBinding.inflate(layoutInflater)

        adapter = NotificationsRecyclerViewAdapter(list, context)

        recyclerView = bind.recyclerViewNotifications
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapter

        getNotifications()

        return bind.root
    }

    private fun getNotifications() {
        friendRequests
        likes
    }

    private val friendRequests: Unit
        get() {
            fStore.collection("Users")
                    .document(auth.uid!!)
                    .addSnapshotListener { ds, e ->
                        if (e != null) {
                            Log.e(ContentValues.TAG, e.stackTrace.toString())
                        } else if (ds!!.exists()) {
                            profile = ds.toObject(Profile::class.java)
                            val map: Map<String, Timestamp>? = profile!!.getFriendRequests()
                            for (UID in map!!.keys) {
                                val friendRequestNotification = FriendRequestNotification()
                                friendRequestNotification.uID = UID
                                friendRequestNotification.date = map[UID]
                                notifications[friendRequestNotification.uID!!] = friendRequestNotification
                                list = ArrayList()
                                list.addAll(notifications.values)
                                list.sortedBy { it!!.date }
                                adapter!!.update(list)
                            }
                        }
                    }
        }
    private val likes: Unit
        get() {
            fStore.collection("Users")
                    .document(auth.uid!!)
                    .collection("Posts")
                    .addSnapshotListener { qs, e ->
                        if (e != null) {
                            Log.e(ContentValues.TAG, e.stackTrace.toString())
                        } else if (!qs!!.isEmpty) {
                            for (ds in qs.documents) {
                                val p = ds.toObject(Post::class.java)
                                val likes: Map<String, Timestamp>? = p!!.likes
                                if (!likes!!.isEmpty()) {
                                    val likeNotification = LikeNotification()
                                    likeNotification.likeNumbers = likes.size
                                    likeNotification.date = likes.values.toTypedArray()[0]
                                    likeNotification.postID = p.Id
                                    likeNotification.uID = p.uId
                                    notifications[likeNotification.postID!!] = likeNotification
                                    list = ArrayList()
                                    list.addAll(notifications.values)
                                    list.sortedBy { it!!.date }
                                    adapter!!.update(list)
                                }
                            }
                        }
                    }
        }
}