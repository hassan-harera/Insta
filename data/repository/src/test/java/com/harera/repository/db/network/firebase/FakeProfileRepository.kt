package com.harera.repository.db.network.firebase

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
import com.harera.model.modelget.FollowRelation as FollowRelationGet
import com.harera.model.modelget.FollowRequest as FollowRequestGet
import com.harera.model.modelget.Profile as ProfileGet

class FakeProfileRepository : ProfileRepository {

    override suspend fun getFollowers(uid: String): List<FollowRelationGet> =
        try {
            fStore.collection(FOLLOWERS)
                .whereEqualTo(FollowRelation::followingUid.name, uid)
                .get()
                .result
                .map {
                    it.toObject(FollowRelationGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getFollowings(uid: String): List<FollowRelationGet> =
        try {
            fStore
                .collection(FOLLOWERS)
                .whereEqualTo(FollowRelation::followerUid.name, uid)
                .get()
                .result
                .map {
                    it.toObject(FollowRelationGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addProfile(profile: Profile): Boolean =
        try {
            fStore.collection(USERS)
                .document(profile.uid)
                .set(profile)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }

    override suspend fun checkFollowRelation(
        followerUid: String,
        followingUid: String
    ): Boolean =
        try {
            fStore.collection(FOLLOWERS)
                .whereEqualTo(FollowRelation::followerUid.name, followerUid)
                .whereEqualTo(FollowRelation::followingUid.name, followingUid)
                .get()
                .result
                .isEmpty
                .not()
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getMutualFollowings(
        uid: String,
        followingsList: List<String>
    ): List<FollowRelationGet> =
        try {
            fStore
                .collection(FOLLOWERS)
                .whereIn(FollowRelation::followingUid.name, followingsList)
                .whereEqualTo(FollowRelation::followerUid.name, uid)
                .get()
                .result
                .map {
                    it.toObject(FollowRelationGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun updateProfilePicture(imageBitmap: Bitmap, uid: String): Boolean =
        try {
            val inputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, inputStream)

            firebaseStorage
                .reference
                .child(USERS)
                .child(uid)
                .child(PROFILE_PIC)
                .putBytes(inputStream.toByteArray())
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getFollowRequests(uid: String): List<FollowRequestGet> =
        try {
            fStore.collection(USERS)
                .document(uid)
                .collection(FOLLOW_REQUESTS)
                .get()
                .result
                .map {
                    it.toObject(FollowRequestGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun updateProfileImageUrl(uid: String, profileImageUrl: String): Boolean =
        try {
            fStore.collection(USERS)
                .document(uid)
                .update(Profile::profileImageUrl.name, profileImageUrl)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun getProfile(uid: String): ProfileGet =
        try {
            fStore.collection(USERS)
                .document(uid)
                .get()
                .result
                .toObject(ProfileGet::class.java)!!
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addFollowRequest(followRequest: FollowRequest): Boolean =
        try {
            fStore
                .collection(USERS)
                .document(followRequest.toUid)
                .collection(FOLLOW_REQUESTS)
                .document()
                .apply {
                    followRequest.id = id
                }.set(followRequest)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun searchProfiles(searchWord: String): List<ProfileGet> =
        try {
            fStore
                .collection(USERS)
                .whereGreaterThanOrEqualTo(NAME, searchWord)
                .get()
                .result
                .map {
                    it.toObject(ProfileGet::class.java)
                }
        } catch (e: Exception) {
            throw e
        }


    override suspend fun updateProfile(profile: Profile): Boolean =
        try {
            fStore
                .collection(USERS)
                .document(profile.uid)
                .set(profile)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }

    override suspend fun addFollower(followRelation: FollowRelation): Boolean =
        try {
            fStore
                .collection(FOLLOWERS)
                .document()
                .apply {
                    followRelation.followId = id
                }
                .set(followRelation)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }


    override suspend fun addFollowing(followRelation: FollowRelation): Boolean =
        try {
            fStore
                .collection(FOLLOWERS)
                .document()
                .apply {
                    followRelation.followId = id
                }
                .set(followRelation)
                .isSuccessful
        } catch (e: Exception) {
            throw e
        }
}
