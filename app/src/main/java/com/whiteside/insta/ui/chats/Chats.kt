package com.whiteside.insta.ui.chats

import com.whiteside.insta.adapter.ChatsRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.databinding.FragmentChatsBinding

class Chats : Fragment() {

    private lateinit var bind: FragmentChatsBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var friends: List<String> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var adapter: ChatsRecyclerViewAdapter? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        bind = FragmentChatsBinding.inflate(inflater)


        recyclerView = bind.friends
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
//        getChats()

        return bind.root
    }

    private fun getChats() {
        fStore.collection("Users")
                .document(auth.uid!!)
                .get()
                .addOnSuccessListener { ds: DocumentSnapshot ->
                    friends = ds["friends"] as List<String>
                    adapter = ChatsRecyclerViewAdapter(context, friends)
                    recyclerView!!.adapter = adapter
                }
    }

}