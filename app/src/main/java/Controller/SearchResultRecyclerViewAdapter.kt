package Controller

import com.whiteside.insta.ui.edit_profile.Profile
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
import com.whiteside.insta.VisitProfile

class SearchResultRecyclerViewAdapter(list: List<String?>, context: Context) : RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {
    var list: List<String?>
    var context: Context
    private val fStore: FirebaseFirestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ll.setOnClickListener {
            val intent = Intent(context, VisitProfile::class.java)
            intent.putExtra("UID", list[position])
            context.startActivity(intent)
        }
        val UID = list[position]
        fStore.collection("Users")
                .document(UID!!)
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
        return list.size
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
        this.list = list
        this.context = context
    }
}