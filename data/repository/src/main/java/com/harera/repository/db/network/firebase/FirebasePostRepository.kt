package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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
import javax.inject.Inject

class FirebasePostRepository @Inject constructor(
    private val fStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : PostRepository {

    override fun getFeedPosts(followings: List<String>, limit: Int): Task<QuerySnapshot> =
        fStore
            .collection(PSOTS)
            .whereIn(Post::uid.name, followings)
            .limit(15)
            .orderBy(Post::time.name, Query.Direction.DESCENDING)
            .get()

    override fun getPost(postId: String): Task<DocumentSnapshot> =
        fStore.collection(PSOTS)
            .document(postId)
            .get()

    override fun getProfilePosts(uid: String): Task<QuerySnapshot> =
        fStore.collection(PSOTS)
            .whereEqualTo(UID, uid)
            .get()

    override fun updatePost(post: Post): Task<Void> =
        fStore.collection(USERS)
            .document(post.uid)
            .collection(POSTS)
            .document(post.postId)
            .set(post, SetOptions.merge())

    override fun addLike(like: Like): Task<Void> =
        fStore.collection(LIKES)
            .document()
            .apply {
                like.likeId = id
            }
            .set(like)

    override fun removeLike(likeId: String): Task<Void> =
        fStore.collection(LIKES)
            .document(likeId)
            .delete()

    override fun uploadPostImage(imageBitmap: Bitmap, postId: String, uid: String): UploadTask {
        val inputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, inputStream)

        return firebaseStorage
            .reference
            .child(POSTS)
            .child(postId)
            .putBytes(inputStream.toByteArray())
    }

    override fun getPostLikes(postId: String): Task<QuerySnapshot> =
        fStore.collection(LIKES)
            .whereEqualTo(Post::postId.name, postId)
            .get()

    override fun getPostLike(postId: String, uid: String): Task<QuerySnapshot> =
        fStore.collection(LIKES)
            .whereEqualTo(Like::uid.name, uid)
            .whereEqualTo(Like::postId.name, postId)
            .get()

    override fun getPostComments(postId: String): Task<QuerySnapshot> =
        fStore.collection(COMMENTS)
            .whereEqualTo(Post::postId.name, postId)
            .get()

    override fun addComment(comment: Comment): Task<Void> =
        fStore.collection(COMMENTS)
            .document()
            .apply {
                comment.commentId = id
            }
            .set(comment)

//    override fun getNewLikeId(post: Post): String =
//        fStore
//            .collection(USERS)
//            .document(post.uid)
//            .collection(POSTS)
//            .document(post.postId)
//            .collection(LIKES)

    override fun searchPosts(searchWord: String): Task<QuerySnapshot> =
        fStore.collection(POSTS)
            .whereGreaterThanOrEqualTo(Post::caption.name, searchWord)
            .get()

    override fun getNewPostId(uid: String): String =
        fStore.collection(POSTS)
            .document()
            .id

    override fun addPost(post: Post): Task<Void> =
        fStore.collection(POSTS)
            .document(post.postId)
            .set(post)
}