package Controller

import Model.Date_Time.timeFromNow
import Model.Post
import Model.Profile
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.whiteside.insta.R

class PostsRecyclerViewAdapter(var posts: List<Post?>, private val context: Context?) : RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {
    var fStore: FirebaseFirestore
    var auth: FirebaseAuth
    fun update(posts: List<Post?>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_view_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        fStore.collection("Users")
                .document(post!!.uId!!)
                .addSnapshotListener(EventListener { ds, e ->
                    if (e != null) {
                        e.printStackTrace()
                        return@EventListener
                    }
                    val profile = ds!!.toObject(Profile::class.java)
                    holder.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(profile!!.profilePic!!.toBytes(),
                            0, profile.profilePic!!.toBytes().size))
                    holder.profileName.text = profile.name
                    holder.postImage.setImageBitmap(getPostImageAsBitmap(post.postImage))
                    holder.date.text = timeFromNow(post.time!!)
                    holder.caption.text = post.caption
                    holder.love_number.text = post.likes!!.size.toString()
                    holder.love.setImageResource(if (post.liked!!) R.drawable.loved else R.drawable.love)
                    holder.bar.visibility = View.GONE
                })
        holder.love.setOnClickListener {
            holder.love.isEnabled = false
            if (!post.liked!!) {
                post.liked = true
                post.addLike(auth.uid!!)
                holder.love_number.text = post.likes!!.size.toString()
                holder.love.setImageResource(R.drawable.loved)
                setLike(post)
                holder.love.isEnabled = true
            } else {
                post.liked = false
                post.removeLike(auth.uid!!)
                holder.love_number.text = post.likes!!.size.toString()
                holder.love.setImageResource(R.drawable.love)
                removeLike(post)
                holder.love.isEnabled = true
            }
        }
    }

    fun getPostImageAsBitmap(postImage: Blob?): Bitmap {
        val b = postImage!!.toBytes()
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    private fun removeLike(post: Post?) {
        fStore.collection("Users")
                .document(post!!.uId!!)
                .collection("Posts")
                .document(post.Id!!)
                .update("likes", post.likes)
    }

    private fun setLike(post: Post?) {
        fStore.collection("Users")
                .document(post!!.uId!!)
                .collection("Posts")
                .document(post.Id!!)
                .update("likes", post.likes)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bar: ProgressBar
        var date: TextView
        var profileName: TextView
        var caption: TextView
        var love_number: TextView
        var love: ImageView
        var postImage: ImageView
        var profileImage: ImageView

        init {
            caption = itemView.findViewById(R.id.caption)
            postImage = itemView.findViewById(R.id.post_image)
            love_number = itemView.findViewById(R.id.love_number)
            bar = itemView.findViewById(R.id.progress_bar)
            profileImage = itemView.findViewById(R.id.profile_image)
            date = itemView.findViewById(R.id.date)
            profileName = itemView.findViewById(R.id.profile_name)
            love = itemView.findViewById(R.id.love)
        }
    }

    init {
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }
}