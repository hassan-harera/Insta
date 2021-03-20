package com.whiteside.insta.adapter

import android.app.Activity
import android.app.ActivityOptions
import com.whiteside.insta.model.Profile
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.whiteside.insta.R
import com.whiteside.insta.R.*
import com.whiteside.insta.databinding.CardViewProfileResultBinding
import com.whiteside.insta.ui.visit_profile.VisitProfile

class ProfileSearchResultsAdapter(var results: List<Profile?>) :
    RecyclerView.Adapter<ProfileSearchResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bind = DataBindingUtil.inflate<CardViewProfileResultBinding>(
            LayoutInflater.from(parent.context),
            layout.card_view_profile_result,
            parent,
            false
        )
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setProfile(results[position]!!)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class ViewHolder(val bind: CardViewProfileResultBinding) : RecyclerView.ViewHolder(bind.root) {
        fun setProfile(profile: Profile) {
            bind.profile = profile

            bind.profileCard.setOnClickListener {
                val intent = Intent(bind.root.context, VisitProfile::class.java)
                intent.putExtra("UID", profile.uid)
                bind.root.context.startActivity(intent)
            }
        }
    }
}