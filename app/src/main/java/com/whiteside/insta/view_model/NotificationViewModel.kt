package com.whiteside.insta.view_model

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.whiteside.insta.model.*
import com.whiteside.insta.ui.view_post.ViewPost
import com.whiteside.insta.ui.visit_profile.VisitProfile

class NotificationViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore = FirebaseFirestore.getInstance()

    var profile = MutableLiveData<Profile?>()
    var post = MutableLiveData<Post?>()

    var likeNotification: LikeNotification? = null
    var friendRequestNotification: FriendRequestNotification? = null

    val requestConfirmed = MutableLiveData<Boolean>()
    val requestDeleted = MutableLiveData<Boolean>()

    companion object {
        @JvmStatic
        @BindingAdapter("notification_message")
        fun adaptLikeMessage(view: TextView, post: Post?) {
            if (post != null)
                view.text = "${post.likes!!.size} liked your post"
        }

        @JvmStatic
        @BindingAdapter("notification_message")
        fun adaptFriendRequestMessage(view: TextView, profile: Profile?) {
            profile?.let {
                view.text = "${profile.name} sent you a friend request"
            }
        }

        @JvmStatic
        @BindingAdapter("notification_time")
        fun getNotificationDate(view: TextView, notification: FriendRequestNotification?) {
            if (notification != null)
                view.text = Date_Time.timeFromNow(notification.date)
        }

        @JvmStatic
        @BindingAdapter("notification_time")
        fun getNotificationDate(view: TextView, notification: LikeNotification?) {
            if (notification != null)
                view.text = Date_Time.timeFromNow(notification.date)
        }

        @JvmStatic
        @BindingAdapter("confirm")
        fun adaptConfirm(view: TextView, profile: Profile?) {
            if (profile != null)
                view.visibility = View.VISIBLE
        }

        @JvmStatic
        @BindingAdapter("delete")
        fun adaptDelete(view: TextView, profile: Profile?) {
            if (profile != null)
                view.visibility = View.VISIBLE
        }
    }


    fun onPostClicked(view: View) {
        likeNotification?.let {
            val intent = Intent(view.context, ViewPost::class.java)
            intent.putExtra("postID", likeNotification!!.postID)
            intent.putExtra("UID", likeNotification!!.UID)
            view.context.startActivity(intent)
        }
    }

    fun setLikeNotification(notification: Notification?) {
        likeNotification = notification as LikeNotification
    }

    fun setRequestNotification(notification: Notification?) {
        friendRequestNotification = notification as FriendRequestNotification
    }

    fun onConfirmClicked() {
        profile.value?.let {
            it.friends!!.add(auth.uid!!)
            fStore.collection("Users")
                .document(it.uid!!)
                .set(it, SetOptions.merge())
                .addOnSuccessListener {
                    getAuthProfile()
                }
        }
    }

    private fun getAuthProfile() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val userProfile = it.toObject(Profile::class.java)!!
                updateProfile(userProfile)
            }
    }

    private fun updateProfile(userProfile: Profile) {
        userProfile.friends!!.add(profile.value!!.uid!!)
        userProfile.friendRequests!!.remove(profile.value!!.uid)

        fStore.collection("Users")
            .document(auth.uid!!)
            .set(userProfile, SetOptions.merge())
            .addOnSuccessListener {
                requestConfirmed.value = true
            }
    }

    fun onDeleteClicked() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                val userProfile = it.toObject(Profile::class.java)!!
                userProfile.friendRequests!!.remove(profile.value!!.uid!!)

                fStore.collection("Users")
                    .document(userProfile.uid!!)
                    .set(userProfile, SetOptions.merge())
                    .addOnSuccessListener {
                        requestDeleted.value = true
                    }
            }
    }

    fun onProfileClicked(view: View) {
        if (friendRequestNotification != null) {
            val intent = Intent(view.context, VisitProfile::class.java)
            intent.putExtra("UID", friendRequestNotification!!.UID)
            view.context.startActivity(intent)
        }
    }


    fun loadPost() {
        fStore.collection("Users")
            .document(likeNotification!!.UID)
            .collection("Posts")
            .document(likeNotification!!.postID)
            .get()
            .addOnSuccessListener {
                post.value = it.toObject(Post::class.java)
            }
    }

    fun loadProfile() {
        fStore.collection("Users")
            .document(friendRequestNotification!!.UID!!)
            .get()
            .addOnSuccessListener {
                profile.value = it.toObject(Profile::class.java)
            }
    }
}