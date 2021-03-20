package com.whiteside.insta.ui.chat

import com.whiteside.insta.model.Message
import com.whiteside.insta.model.Profile
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.adapter.FriendsRecyclerViewAdapter
import java.io.ByteArrayOutputStream

class ChatViewModel : ViewModel() {
    private val fStore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()


    val profile = MutableLiveData<Profile>()
    lateinit var UID: String
    val messages = MutableLiveData<ArrayList<Message>>()
    val message = Message()

    private val friends = ArrayList<Profile>()
    val friendsAdapter = FriendsRecyclerViewAdapter(friends)

    init {
        messages.value = ArrayList()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("friends")
        fun adaptFriends(view: RecyclerView, adapter: FriendsRecyclerViewAdapter) {
            view.adapter = adapter
        }
    }

    fun sendMessageClicked(message: Message) {
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
                    val bitmap = BitmapFactory.decodeByteArray(
                        profile!!.profilePic!!.toBytes(),
                        0,
                        profile.profilePic!!.toBytes().size
                    )

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

    fun getFriends() {
        fStore.collection("Users")
            .document(auth.uid!!)
            .get()
            .addOnSuccessListener {
                it.toObject(Profile::class.java)!!.friends!!.forEach {

                    fStore.collection("Users")
                        .document(it)
                        .get()
                        .addOnSuccessListener {
                            it.toObject(Profile::class.java)!!
                            friends.add(it.toObject(Profile::class.java)!!)
                            friendsAdapter.notifyDataSetChanged()
                        }
                }
            }
    }
}