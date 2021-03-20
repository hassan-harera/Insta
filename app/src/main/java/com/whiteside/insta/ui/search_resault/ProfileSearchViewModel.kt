package com.whiteside.insta.ui.search_resault

import android.content.Intent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.whiteside.insta.adapter.ProfileSearchResultsAdapter
import com.whiteside.insta.model.Profile
import com.whiteside.insta.ui.chat.ChatActivity
import kotlin.collections.ArrayList

class ProfileSearchViewModel : ViewModel() {
    private var profileResults: MutableList<Profile?> = ArrayList()
    var adapter = ProfileSearchResultsAdapter(profileResults)

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val fStore = FirebaseFirestore.getInstance()

    val emptyResult = MutableLiveData<Boolean>()

    companion object {
        @JvmStatic
        @BindingAdapter("profile_results")
        fun loadProfilePic(view: RecyclerView, adapter: ProfileSearchResultsAdapter?) {
            view.adapter = adapter
        }
    }

    fun onChatClicked(view: View, profile: Profile?) {
        profile?.let {
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra("UID", it.uid)
            view.context.startActivity(intent)
        }
    }

    fun loadSearchResults(profileName: String) {
        FirebaseFirestore
            .getInstance()
            .collection("Users")
            .whereGreaterThan("name", profileName)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    profileResults.add(it.toObject(Profile::class.java))
                    adapter.notifyDataSetChanged()
                }

                if (it.documents.isEmpty())
                    emptyResult.value = true
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}