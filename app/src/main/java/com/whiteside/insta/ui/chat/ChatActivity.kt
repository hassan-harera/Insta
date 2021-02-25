package com.whiteside.insta.ui.chat

import Controller.MessagesRecyclerViewAdapter
import Model.Message
import com.whiteside.insta.ui.edit_profile.Profile
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.R
import com.whiteside.insta.databinding.ActivityChatBinding
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private val messages: ArrayList<Message> = ArrayList()
    private var UID: String? = null
    private var adapter: MessagesRecyclerViewAdapter? = null
    private var auth: FirebaseAuth? = null
    private var dRef: DatabaseReference? = null
    private var fStore: FirebaseFirestore? = null
    private var bind: ActivityChatBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        sendClicked
        bind = DataBindingUtil.setContentView(this, R.layout.activity_chat)


        setSupportActionBar(bind!!.appBar)

        UID = intent.extras!!.getString("UID")
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        dRef = FirebaseDatabase.getInstance().reference
        bind!!.recyclerView.setHasFixedSize(true)
        bind!!.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessagesRecyclerViewAdapter(messages, this)
        bind!!.recyclerView.adapter = adapter
        fStore!!.collection("Users")
                .document(UID!!)
                .addSnapshotListener { ds, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, e.stackTrace.toString())
                    } else if (ds!!.exists()) {
                        val profile = ds.toObject(Profile::class.java)
                        val bitmap = BitmapFactory.decodeByteArray(profile!!.profilePic!!.toBytes(), 0, profile.profilePic!!.toBytes().size)
                        bind!!.profileImage.setImageBitmap(bitmap)
                        bind!!.name.text = profile.name
                    }
                }
        dRef!!.child("Users")
                .child(auth!!.uid!!)
                .child("Chats")
                .child(UID!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var c = 1
                        for (s in snapshot.children) {
                            if (c > messages.size) {
                                val m = s.getValue(Message::class.java)
                                messages.add(m!!)
                                adapter!!.notifyDataSetChanged()
                                bind!!.recyclerView.smoothScrollToPosition(messages.size)
                            }
                            c++
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
    }
}