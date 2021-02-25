package com.whiteside.insta.ui.chat

import Model.Message
import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    fun sendClicked(view: View, message: Message) {
        val messageText = bind!!.messageSend.text.toString()
        if (messageText != "") {
            val m = Message()
            m.from = auth!!.uid
            m.to = UID
            m.seconds = Timestamp.now().seconds
            m.message = bind!!.messageSend.text.toString()
            sendMessage(m)
        }
    }

    protected fun sendMessage(m: Message): Boolean {
        var dbReferences = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val uid = Objects.requireNonNull(auth.uid)
        dbReferences = dbReferences
                .child("Users")
                .child(uid!!)
                .child("Chats")
                .child(m.to!!)
                .push()
        dbReferences.setValue(m)
        dbReferences
                .child("Users")
                .child(m.to!!)
                .child("Chats")
                .child(uid)
                .push()
                .setValue(m)
        return true
    }
}