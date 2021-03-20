package com.whiteside.insta.ui.notifications

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.whiteside.insta.adapter.NotificationsRecyclerViewAdapter
import com.whiteside.insta.model.*
import java.sql.Time
import kotlin.collections.ArrayList

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    var notifications: ArrayList<Notification> = ArrayList()
    val adapter = NotificationsRecyclerViewAdapter(notifications)

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        //TODO recycler view Binding Adapter
        @JvmStatic
        @BindingAdapter("notifications")
        fun loadAdapter(view: RecyclerView, adapter: NotificationsRecyclerViewAdapter) {
            view.adapter = adapter
        }
    }

    fun getFriendRequests() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val profile = it.toObject(Profile::class.java)

                profile!!.friendRequests!!.forEach {
                    notifications.add(
                        FriendRequestNotification(
                            it.key,
                            it.value
                        )
                    )
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getLikesNotifications() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .collection("Posts")
            .whereGreaterThan("likes", HashMap<String, Timestamp>())
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val post = it.toObject(Post::class.java)
                    notifications.add(LikeNotification(post!!.Id!!, post.UID!!, post.likes!!.values.last()))
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}