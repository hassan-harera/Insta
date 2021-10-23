package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.harera.model.modelset.Comment
import com.harera.model.modelset.Like
import com.harera.model.modelset.Post
import com.harera.repository.common.Constansts.COMMENTS
import com.harera.repository.common.Constansts.LIKES
import com.harera.repository.common.Constansts.POSTS
import com.harera.repository.common.Constansts.PSOTS
import com.harera.repository.common.Constansts.UID
import com.harera.repository.common.Constansts.USERS
import com.harera.repository.db.network.abstract_.PostRepository
import java.io.ByteArrayOutputStream
import com.harera.model.modelget.Comment as CommentGet
import com.harera.model.modelget.Like as LikeGet
import com.harera.model.modelget.Post as PostGet


class FirebasePostRepository constructor(
    private val fStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : PostRepository {

    override suspend fun getFeedPosts(followings: List<String>, limit: Int): List<PostGet> =
        try {
            fStore
                .collection(PSOTS)
                .whereIn(Post::uid.name, followings)
                .limit(15)
                .orderBy(Post::time.name, Query.Direction.DESCENDING)
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getPost(postId: String) =
        try {
            fStore.collection(PSOTS)
                .document(postId)
                .get()
                .result
                .toObject(PostGet::class.java)
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getUserPosts(uid: String, limit: Int): List<PostGet> =
        try {
            fStore.collection(PSOTS)
                .whereEqualTo(UID, uid)
                .limit(limit.toLong())
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun updatePost(post: Post): Boolean =
        try {
            fStore.collection(USERS)
                .document(post.uid)
                .collection(POSTS)
                .document(post.postId)
                .set(post, SetOptions.merge())
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addLike(like: Like): Boolean =
        try {
            fStore.collection(LIKES)
                .document()
                .apply {
                    like.likeId = id
                }
                .set(like)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun removeLike(likeId: String): Boolean =
        try {
            fStore.collection(LIKES)
                .document(likeId)
                .delete()
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun uploadPostImage(
        imageBitmap: Bitmap,
        postId: String,
        uid: String
    ): Boolean =
        try {
            val inputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, inputStream)

            firebaseStorage
                .reference
                .child(POSTS)
                .child(postId)
                .putBytes(inputStream.toByteArray())
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getPostLikes(postId: String): List<PostGet>  =
        try {
            fStore.collection(LIKES)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getPostLike(postId: String, uid: String): LikeGet =
        try {
            fStore.collection(LIKES)
                .whereEqualTo(Like::uid.name, uid)
                .whereEqualTo(Like::postId.name, postId)
                .get()
                .result
                .first()
                .toObject(LikeGet::class.java)
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getPostComments(postId: String): List<CommentGet> =
        try {
            fStore.collection(COMMENTS)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .result
                .map {
                    it.toObject(CommentGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addComment(comment: Comment): Boolean  =
        try {
            fStore.collection(COMMENTS)
                .document()
                .apply {
                    comment.commentId = id
                }
                .set(comment)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun searchPosts(searchWord: String): List<PostGet> =
        try {
            fStore.collection(POSTS)
                .whereGreaterThanOrEqualTo(Post::caption.name, searchWord)
                .get()
                .result
                .map {
                    it.toObject(PostGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getNewPostId(uid: String): String =
        try {
            fStore.collection(POSTS)
                .document()
                .id
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addPost(post: Post): Boolean =
        try {
            fStore.collection(POSTS)
                .document(post.postId)
                .set(post)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }

}

