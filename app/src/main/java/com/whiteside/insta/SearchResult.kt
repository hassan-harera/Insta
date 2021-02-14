package com.whiteside.insta

import Controller.ChatsRecyclerViewAdapter
import Controller.SearchResultRecyclerViewAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SearchResult : AppCompatActivity() {
    private val friends: List<String>? = null
    private var recyclerView: RecyclerView? = null
    private val adapter: ChatsRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        recyclerView = findViewById(R.id.friends)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        val list = intent.extras!!.getStringArray("list")
        recyclerView!!.setAdapter(SearchResultRecyclerViewAdapter(Arrays.asList(*list), this))
    }
}