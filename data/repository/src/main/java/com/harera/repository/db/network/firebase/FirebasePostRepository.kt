package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.harera.model.model.Comment
import com.harera.model.model.Like
import com.harera.model.model.Post
import com.harera.repository.common.Constansts.COMMENTS
import com.harera.repository.common.Constansts.LIKES
import com.harera.repository.common.Constansts.POSTS
import com.harera.repository.common.Constansts.PSOTS
import com.harera.repository.common.Constansts.UID
import com.harera.repository.common.Constansts.USERS
import com.harera.repository.db.network.abstract_.PostRepository
import java.io.ByteArrayOutputStream


class FirebasePostRepository constructor(
    private val fStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : PostRepository {

    override suspend fun getFeedPosts(
        followings: List<String>,
        limit: Int
    ): Result<List<Post>> =
        kotlin.runCatching {
            fStore
                .collection(PSOTS)
                .whereIn(Post::uid.name, followings)
                .limit(15)
                .orderBy(Post::time.name, Query.Direction.DESCENDING)
                .get()
                .let {
                    Tasks.await(it)
                }
                .map {
                    it.toObject(Post::class.java)
                }
        }

    override suspend fun getPost(postId: String) = kotlin.runCatching {
        fStore.collection(PSOTS)
            .document(postId)
            .get()
            .let {
                Tasks.await(it)
            }
            .toObject(Post::class.java)
    }

    override suspend fun getUserPosts(
        uid: String,
        limit: Int
    ): Result<List<Post>> = kotlin.runCatching {
        fStore.collection(PSOTS)
            .whereEqualTo(UID, uid)
            .orderBy(Post::time.name, Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .let {
                Tasks.await(it)
            }
            .map {
                it.toObject(Post::class.java)
            }
    }

    override suspend fun updatePost(post: Post): Result<Boolean> = kotlin.runCatching {
        fStore.collection(USERS)
            .document(post.uid)
            .collection(POSTS)
            .document(post.postId)
            .set(post, SetOptions.merge())
            .let {
                Tasks.await(it)
                it.isSuccessful
            }
    }

    override suspend fun addLike(like: Like): Result<Boolean> = kotlin.runCatching {
        fStore.collection(LIKES)
            .document()
            .apply {
                like.likeId = id
            }
            .set(like)
            .let {
                Tasks.await(it)
                it.isSuccessful
            }
    }


    override suspend fun removeLike(postId: String, uid: String): Result<Boolean> =
        kotlin.runCatching {
            fStore.collection(LIKES)
                .whereEqualTo(Like::postId.name, postId)
                .whereEqualTo(Like::uid.name, uid)
                .get()
                .let {
                    return@let Tasks.await(it)
                }.let {
                    fStore.collection(LIKES)
                        .document(it.documents[0].id)
                        .delete()
                        .let {
                            it.isSuccessful
                        }
                }
        }

    override suspend fun uploadPostImage(
        imageBitmap: Bitmap,
        postId: String,
        uid: String
    ): Result<String> = kotlin.runCatching {
        val inputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, inputStream)

        firebaseStorage
            .reference
            .child(POSTS)
            .child(postId)
            .putBytes(inputStream.toByteArray())
            .let {
                Tasks.await(it)
            }
            .storage
            .downloadUrl
            .let {
                Tasks.await(it)
            }
            .toString()
    }


    override suspend fun uploadPostImage(
        imageUri: Uri,
        postId: String,
        uid: String
    ): Result<String> = kotlin.runCatching {
        firebaseStorage
            .reference
            .child(POSTS)
            .child(postId)
            .putFile(imageUri)
            .let {
                Tasks.await(it)
            }
            .storage
            .downloadUrl
            .let {
                Tasks.await(it)
            }
            .toString()
    }


    override suspend fun getPostLikes(postId: String): List<Post> =
        try {
            fStore.collection(LIKES)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .let {
                    Tasks.await(it)
                }
                .map {
                    it.toObject(Post::class.java)
                }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getPostLike(
        postId: String,
        uid: String
    ): Result<Like?> = kotlin.runCatching {
        fStore.collection(LIKES)
            .whereEqualTo(Like::uid.name, uid)
            .whereEqualTo(Like::postId.name, postId)
            .get()
            .let {
                Tasks.await(it)
            }.firstOrNull()
            ?.toObject(Like::class.java)
    }


    override suspend fun getPostComments(postId: String): List<Comment> =
        try {
            fStore.collection(COMMENTS)
                .whereEqualTo(Post::postId.name, postId)
                .get()
                .let {
                    Tasks.await(it)
                }
                .map {
                    it.toObject(Comment::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addComment(comment: Comment): Result<Boolean> = kotlin.runCatching {
        fStore.collection(COMMENTS)
            .document()
            .apply {
                comment.commentId = id
            }
            .set(comment)
            .let {
                Tasks.await(it)
                it.isSuccessful
            }
    }

    override suspend fun searchPosts(searchWord: String): Result<List<Post>> = kotlin.runCatching {
        fStore.collection(POSTS)
            .whereGreaterThanOrEqualTo(Post::caption.name, searchWord)
            .get()
            .let {
                Tasks.await(it)
            }
            .map {
                it.toObject(Post::class.java)
            }
    }

    override suspend fun getNewPostId(uid: String): Result<String> = kotlin.runCatching {
        fStore.collection(POSTS)
            .document()
            .id
    }


    override suspend fun addPost(post: Post): Result<Boolean> = kotlin.runCatching {
        fStore.collection(POSTS)
            .document(post.postId)
            .set(post)
            .let {
                Tasks.await(it)
                it.isSuccessful
            }
    }
}