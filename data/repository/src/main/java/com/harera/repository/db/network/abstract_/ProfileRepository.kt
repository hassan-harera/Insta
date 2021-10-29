package com.harera.repository.db.network.abstract_

import android.graphics.Bitmap
import com.harera.model.model.FollowRelation
import com.harera.model.model.FollowRequest
import com.harera.model.model.Profile

interface ProfileRepository {

    suspend fun addFollowRequest(followRequest: FollowRequest): Boolean
    suspend fun searchProfiles(searchWord: String): List<Profile>
    suspend fun updateProfile(profile: Profile): Boolean
    suspend fun addFollower(followRelation: FollowRelation): Boolean
    suspend fun addFollowing(followRelation: FollowRelation): Boolean
    suspend fun getFollowings(uid: String): List<FollowRelation>
    suspend fun addProfile(profile: Profile): Boolean
    suspend fun getFollowRequests(uid: String): List<FollowRequest>
    suspend fun getFollowers(uid: String): List<FollowRelation>
    suspend fun getProfile(uid: String): Profile
    suspend fun updateProfilePicture(imageBitmap: Bitmap, uid: String): Boolean
    suspend fun updateProfileImageUrl(uid: String, profileImageUrl: String): Boolean
    suspend fun checkFollowRelation(followerUid: String, followingUid: String): Boolean
    suspend fun getMutualFollowings(uid: String, followingsList: List<String>): List<FollowRelation>
}
