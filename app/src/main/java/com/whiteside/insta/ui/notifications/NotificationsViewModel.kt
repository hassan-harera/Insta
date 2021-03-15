package com.whiteside.insta.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.whiteside.insta.model.*
import kotlin.collections.ArrayList

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    var notifications: ArrayList<Notification?> = ArrayList()
    var friendRequestNotifications: ArrayList<FriendRequestNotification?> = ArrayList()
    var likeNotifications: ArrayList<LikeNotification> = ArrayList()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var profile: Profile = Profile()

    companion object {
        //TODO recycler view Binding Adapter
    }

    fun getProfile() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                profile = it.toObject(Profile::class.java)!!
                getFriendRequests()
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getFriendRequests() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .collection(Firebase.FRIEND_REQUESTS_COLLECTION)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    friendRequestNotifications.add(it.toObject(FriendRequestNotification::class.java))
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun getLikesNotifications() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .collection("Likes")
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val like = it.toObject(LikeNotification::class.java)
//
//                    val likeNotification = LikeNotification(
//                        post.Id ?: "",
//                        post.uId ?: "",
//                        likes.size,
//                        likes.values.last(),
//                    )
                    notifications.plus(like)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}