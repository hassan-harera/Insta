package com.harera.repository.db.network.abstract_

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import com.harera.model.modelset.Comment
import com.harera.model.modelset.Like
import com.harera.model.modelset.Post

interface PostRepository {

    fun getFeedPosts(followings: List<String>, limit: Int): Task<QuerySnapshot>
    fun getPost(postId: String): Task<DocumentSnapshot>
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
    fun getUserPosts(uid: String, limit: Int = 5) : Task<QuerySnapshot>
}
