package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.harera.model.modelset.FollowRelation
import com.harera.model.modelset.FollowRequest
import com.harera.model.modelset.Profile
import com.harera.repository.common.Constansts.FOLLOWERS
import com.harera.repository.common.Constansts.FOLLOW_REQUESTS
import com.harera.repository.common.Constansts.NAME
import com.harera.repository.common.Constansts.PROFILE_PIC
import com.harera.repository.common.Constansts.USERS
import com.harera.repository.db.network.abstract_.ProfileRepository
import java.io.ByteArrayOutputStream

class FirebaseProfileRepository  constructor(
    private val firebaseStorage: FirebaseStorage,
    private val fStore: FirebaseFirestore
) : ProfileRepository {

    override fun getFollowers(uid: String): Task<QuerySnapshot> =
        fStore.collection(FOLLOWERS)
            .whereEqualTo(FollowRelation::followingUid.name, uid)
            .get()

    override fun getFollowings(uid: String): Task<QuerySnapshot> =
        fStore
            .collection(FOLLOWERS)
            .whereEqualTo(FollowRelation::followerUid.name, uid)
            .get()

    override fun addProfile(profile: Profile): Task<Void> =
        fStore.collection(USERS)
            .document(profile.uid)
            .set(profile)

    override fun checkFollowRelation(
        followerUid: String,
        followingUid: String
    ): Task<QuerySnapshot> =
        fStore.collection(FOLLOWERS)
            .whereEqualTo(FollowRelation::followerUid.name, followerUid)
            .whereEqualTo(FollowRelation::followingUid.name, followingUid)
            .get()

    override fun getMutualFollowings(uid: String, followingsList: List<String>) =
        fStore
            .collection(FOLLOWERS)
            .whereIn(FollowRelation::followingUid.name, followingsList)
            .whereEqualTo(FollowRelation::followerUid.name, uid)
            .get()

    override fun getFollower(followingUid: String, followerUid: String) =
        fStore
            .collection(FOLLOWERS)
            .whereEqualTo(FollowRelation::followerUid.name, followerUid)
            .whereEqualTo(FollowRelation::followingUid.name, followingUid)
            .get()

    override fun updateProfilePicture(imageBitmap: Bitmap, uid: String): UploadTask {
        val inputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, inputStream)

        return firebaseStorage
            .reference
            .child(USERS)
            .child(uid)
            .child(PROFILE_PIC)
            .putBytes(inputStream.toByteArray())
    }

    override fun getFollowRequests(uid: String): Task<QuerySnapshot> =
        fStore.collection(USERS)
            .document(uid)
            .collection(FOLLOW_REQUESTS)
            .get()

    override fun updateProfileImageUrl(uid: String, profileImageUrl: String): Task<Void> =
        fStore.collection(USERS)
            .document(uid)
            .update(Profile::profileImageUrl.name, profileImageUrl)

    override fun getProfile(uid: String): Task<DocumentSnapshot> =
        fStore.collection(USERS)
            .document(uid)
            .get()

    override fun addFollowRequest(followRequest: FollowRequest): Task<Void> =
        fStore
            .collection(USERS)
            .document(followRequest.toUid)
            .collection(FOLLOW_REQUESTS)
            .document()
            .apply {
                followRequest.id = id
            }.set(followRequest)

    override fun searchProfiles(searchWord: String): Task<QuerySnapshot> =
        fStore
            .collection(USERS)
            .whereGreaterThanOrEqualTo(NAME, searchWord)
            .get()

    override fun updateProfile(profile: Profile): Task<Void> =
        fStore
            .collection(USERS)
            .document(profile.uid)
            .set(profile)

    override fun addFollower(followRelation: FollowRelation) =
        fStore
            .collection(FOLLOWERS)
            .document()
            .apply {
                followRelation.followId = id
            }
            .set(followRelation)

    override fun addFollowing(followRelation: FollowRelation) =
        fStore
            .collection(FOLLOWERS)
            .document()
            .apply {
                followRelation.followId = id
            }
            .set(followRelation)
}
