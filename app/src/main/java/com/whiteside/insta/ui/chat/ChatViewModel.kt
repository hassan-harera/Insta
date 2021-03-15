package com.whiteside.insta.ui.chat

import com.whiteside.insta.adapter.MessagesRecyclerViewAdapter
import com.whiteside.insta.model.Message
import com.whiteside.insta.model.Profile
import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.QuickContactBadge
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    val profile = MutableLiveData<Profile>()
    lateinit var UID: String
    val auth = FirebaseAuth.getInstance()
    val messages = MutableLiveData<ArrayList<Message>>()
    val message = Message()

    init {
        messages.value = ArrayList()
    }

    companion object {
        private val TAG = "CHAT VIEW MODEL"

        @JvmStatic
        @BindingAdapter("profile_image")
        fun getProfileImage(view: QuickContactBadge, profile: Profile?) {
            if (profile == null) {
                return
            }
            val bytes = profile.profilePic!!.toBytes()
            view.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }

        @JvmStatic
        @BindingAdapter("messages")
        fun getMessages(view: RecyclerView, UID: String) {
            FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child("Chats")
                    .child(UID)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (message in snapshot.children) {
                                val mMessage = message.getValue(Message::class.java)
                                (view.adapter as MessagesRecyclerViewAdapter).add(mMessage!!)
                                view.adapter!!.notifyDataSetChanged()
                            }
                        }

                        override fun onCancelled(e: DatabaseError) {
                            Log.e(TAG, "onCancelled: ${e.message}")
                        }
                    })
        }
    }

    fun sendMessageClicked( message: Message) {
        if (!message.message.equals("")) {
            message.from = auth.uid
            message.to = UID
            message.seconds = Timestamp.now().seconds
            saveMessage(message)
        }
    }

    fun getProfile() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID)
                .addSnapshotListener { ds, e ->
                    if (e != null) {
                        Log.e(ContentValues.TAG, e.stackTrace.toString())
                    } else if (ds!!.exists()) {
                        val profile = ds.toObject(Profile::class.java)
                        val bitmap = BitmapFactory.decodeByteArray(profile!!.profilePic!!.toBytes(), 0, profile.profilePic!!.toBytes().size)

                        val stream = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        profile.profilePic = Blob.fromBytes(byteArray)
                    }
                }
    }

    private fun saveMessage(m: Message): Boolean {
        var dbReferences = FirebaseDatabase.getInstance().reference
        dbReferences = dbReferences
                .child("Users")
                .child(auth.uid!!)
                .child("Chats")
                .child(m.to!!)
                .push()

        dbReferences.setValue(m)
        dbReferences
                .child("Users")
                .child(m.to!!)
                .child("Chats")
                .child(auth.uid!!)
                .push()
                .setValue(m)
        return true
    }
}