package Controller

import Controller.MessagesRecyclerViewAdapter.MessageHolder
import Model.Message
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.whiteside.insta.R

class MessagesRecyclerViewAdapter(var messages: List<Message>, var context: Context) : RecyclerView.Adapter<MessageHolder>() {
    var auth: FirebaseAuth
    var currentUID: String?
    var dRef: DatabaseReference
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].from == auth.uid) {
            1
        } else {
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return if (viewType == 1) {
            SenderMessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.sender_message_card_view,
                    parent, false))
        } else {
            ReceiverMessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.reciever_message_card_view,
                    parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val h = holder as SenderMessageHolder
            h.message!!.text = messages[position].message
        } else {
            val h = holder as ReceiverMessageHolder
            h.message!!.text = messages[position].message
        }
    }

    override fun getItemCount(): Int {
        if (messages.isEmpty()) {
            Toast.makeText(context, "No Messages", Toast.LENGTH_SHORT).show()
        }
        return messages.size
    }

    open inner class MessageHolder(v: View) : RecyclerView.ViewHolder(v) {
        var message: TextView? = null
    }

    inner class SenderMessageHolder(v: View) : MessageHolder(v) {
        init {
            message = v.findViewById(R.id.sender_message)
        }
    }

    inner class ReceiverMessageHolder(v: View) : MessageHolder(v) {
        init {
            message = v.findViewById(R.id.receiver_message)
        }
    }

    init {
        auth = FirebaseAuth.getInstance()
        currentUID = auth.uid
        dRef = FirebaseDatabase.getInstance().reference
    }
}