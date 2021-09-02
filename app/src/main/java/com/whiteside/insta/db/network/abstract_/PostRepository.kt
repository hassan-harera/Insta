package com.whiteside.insta.db.network.abstract_

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import com.whiteside.insta.modelset.Comment
import com.whiteside.insta.modelset.Like
import com.whiteside.insta.modelset.Post

interface PostRepository {

    fun getFeedPosts(followings: List<String>, limit: Int): Task<QuerySnapshot>
    fun getPost(postId: String): Task<DocumentSnapshot>
    fun getProfilePosts(uid: String): Task<QuerySnapshot>
    fun updatePost(post: Post): Task<Void>
    fun searchPosts(searchWord: String): Task<QuerySnapshot>
    fun getNewPostId(uid: String): String
    fun addPost(post: Post): Task<Void>
    fun addLike(like: Like): Task<Void>
    fun removeLike(likeId: String): Task<Void>
    fun uploadPostImage(imageBitmap: Bitmap, postId: String, uid: String): UploadTask
    fun getPostLikes(postId: String): Task<QuerySnapshot>
    fun getPostComments(postId: String): Task<QuerySnapshot>
    fun addComment(comment: Comment): Task<Void>
    fun getPostLike(postId: String, uid: String): Task<QuerySnapshot>
}
