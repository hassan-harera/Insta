package com.whiteside.insta.adapter

import com.whiteside.insta.model.Date_Time.timeFromNow
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.whiteside.insta.R
import com.whiteside.insta.model.*
import com.whiteside.insta.ui.view_post.ViewPost
import com.whiteside.insta.ui.visit_profile.VisitProfile

class NotificationsRecyclerViewAdapter(
    var notifications: ArrayList<Notification?>,
    var context: Context?
) : RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder>() {
    var auth: FirebaseAuth
    private val fStore: FirebaseFirestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            LikeHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_view_like, parent, false)
            )
        } else {
            FriendRequestHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_view_friend_request, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return notifications[position]!!.type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = notifications[position]
        when (n!!.type) {
            1 -> {
                val lh = holder as LikeHolder
                val ln = n as LikeNotification?
                lh.relativeLayout.setOnClickListener {
                    val intent = Intent(context, ViewPost::class.java)
                    intent.putExtra("Post ID", ln!!.postID)
                    intent.putExtra("UID", ln.UID)
                    context!!.startActivity(intent)
                }
                fStore.collection("Users")
                    .document(auth.uid!!)
                    .collection("Posts")
                    .document(ln!!.postID!!)
                    .addSnapshotListener { ds, e ->
                        if (e != null) {
                            Log.e(ContentValues.TAG, e.stackTrace.toString())
                        } else if (ds!!.exists()) {
                            val post = ds.toObject(Post::class.java)
                            lh.postIMG.setImageBitmap(
                                BitmapFactory.decodeByteArray(
                                    post!!.postImage!!.toBytes(),
                                    0,
                                    post.postImage!!.toBytes().size
                                )
                            )
                            lh.date.text = timeFromNow(ln.date!!)
                            lh.message.text =
                                post.likes!!.size.toString() + " people reacted to your post"
                        }
                    }
            }
            2 -> {
                val fh = holder as FriendRequestHolder
                val frn = n as FriendRequestNotification?
                fh.relativeLayout.setOnClickListener {
                    val intent = Intent(context, VisitProfile::class.java)
                    intent.putExtra("UID", frn!!.UID)
                    context!!.startActivity(intent)
                }
                fh.confirm.setOnClickListener {
                    confirm(frn)
                    notifications.remove(frn!!.UID)
                    notifyDataSetChanged()
                }
                fh.delete.setOnClickListener {
                    delete(frn)
                    notifications.remove(frn!!.UID)
                    notifyDataSetChanged()
                }
                fStore.collection("Users")
                    .document(frn!!.UID!!)
                    .addSnapshotListener { ds, e ->
                        if (e != null) {
                            Log.e(ContentValues.TAG, e.stackTrace.toString())
                        } else if (ds!!.exists()) {
                            val profile = ds.toObject(Profile::class.java)
                            fh.profilePic.setImageBitmap(
                                BitmapFactory.decodeByteArray(
                                    profile!!.profilePic!!.toBytes(), 0,
                                    profile.profilePic!!.toBytes().size
                                )
                            )
                            fh.date.text = timeFromNow(frn.date!!)
                            fh.message.text = "${profile.name!!} sent you a friend request"
                        }
                    }
            }
        }
    }

    private fun confirm(n: FriendRequestNotification?) {
        val profile = arrayOfNulls<Profile>(1)
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener { ds ->
                profile[0] = ds.toObject(Profile::class.java)
                profile[0]!!.addFriend(n!!.UID!!)
                profile[0]!!.removeFriendRequest(n.UID!!)
                fStore.collection("Users")
                    .document(auth.uid!!)[profile[0]!!] = SetOptions.merge()
            }
        fStore.collection("Users")
            .document(n!!.UID!!)
            .get()
            .addOnSuccessListener { ds ->
                profile[0] = ds.toObject(Profile::class.java)
                profile[0]!!.addFriend(auth.uid!!)
                fStore.collection("Users")
                    .document(n.UID!!)[profile[0]!!] = SetOptions.merge()
            }
    }

    private fun delete(n: FriendRequestNotification?) {
        val profile = arrayOfNulls<Profile>(1)
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener { ds ->
                profile[0] = ds.toObject(Profile::class.java)
                profile[0]!!.removeFriendRequest(n!!.UID!!)
            }
        fStore.collection("Users")
            .document(auth.uid!!)[profile[0]!!] = SetOptions.merge()
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun update(list: ArrayList<Notification?>) {
        notifications = list
        notifyDataSetChanged()
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class LikeHolder(v: View) : ViewHolder(v) {
        var postIMG: ImageView
        var message: TextView
        var date: TextView
        var relativeLayout: RelativeLayout

        init {
            postIMG = v.findViewById(R.id.profile_image)
            message = v.findViewById(R.id.request_message)
            date = v.findViewById(R.id.request_date)
            relativeLayout = v.findViewById(R.id.relativeLayout)
        }
    }

    inner class FriendRequestHolder(v: View) : ViewHolder(v) {
        var profilePic: ImageView
        var message: TextView
        var date: TextView
        var confirm: TextView
        var delete: TextView
        var relativeLayout: RelativeLayout

        init {
            profilePic = v.findViewById(R.id.profile_image)
            message = v.findViewById(R.id.request_message)
            relativeLayout = v.findViewById(R.id.relativeLayout)
            confirm = v.findViewById(R.id.confirm)
            date = v.findViewById(R.id.request_date)
            delete = v.findViewById(R.id.delete)
        }
    }

    init {
        if (notifications.isEmpty()) {
            Toast.makeText(context, "No Notifications To View", Toast.LENGTH_SHORT).show()
        }
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }
}