package com.whiteside.insta.adapter

import com.whiteside.insta.model.Profile
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.QuickContactBadge
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.R
import com.whiteside.insta.ui.chat.ChatActivity

class ChatsRecyclerViewAdapter(var context: Context?, var friends: List<String>) : RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder>() {
    var fStore: FirebaseFirestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_view_chat, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ll.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("UID", friends[position])
            context!!.startActivity(intent)
        }
        val UID = friends[position]
        fStore.collection("Users")
                .document(UID)
                .addSnapshotListener { ds, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, e.stackTrace.toString())
                    } else if (ds!!.exists()) {
                        val profile = ds.toObject(Profile::class.java)
                        val bitmap = BitmapFactory.decodeByteArray(profile!!.profilePic!!.toBytes(), 0, profile.profilePic!!.toBytes().size)
                        holder.badge.setImageBitmap(bitmap)
                        holder.name.text = profile.name
                    }
                }
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var badge: QuickContactBadge
        var name: TextView
        var ll: LinearLayout

        init {
            badge = v.findViewById(R.id.profile_image)
            name = v.findViewById(R.id.name)
            ll = v.findViewById(R.id.relativeLayout)
        }
    }

    init {
        fStore = FirebaseFirestore.getInstance()
    }
}