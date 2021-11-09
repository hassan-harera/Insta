package com.harera.repository

import android.graphics.Bitmap
import com.harera.model.model.FollowRequest
import com.harera.model.model.User
import com.harera.model.response.Connection

interface ProfileRepository {
    fun getProfileUser(token: String?): User?
    suspend fun getProfile(token: String): Result<User>
    suspend fun getUser(token: String, username: String): Result<User>
    suspend fun getFollowings(uid: String): List<Connection>
    suspend fun getFollowers(uid: String): List<Connection>
    suspend fun getMutualFollowings(uid: String, followingsList: List<String>): List<Connection>
    suspend fun getFollowRequests(uid: String): List<FollowRequest>
    suspend fun addFollowRequest(followRequest: FollowRequest): Boolean
    suspend fun searchProfiles(searchWord: String): List<User>
    suspend fun updateProfile(user: User): Boolean
    suspend fun addFollower(followRelation: Connection): Boolean
    suspend fun addFollowing(followRelation: Connection): Boolean
    suspend fun addProfile(user: User): Boolean
    suspend fun updateProfilePicture(imageBitmap: Bitmap, uid: String): Boolean
    suspend fun updateProfileImageUrl(uid: String, profileImageUrl: String): Boolean
    suspend fun checkFollowRelation(followerUid: String, followingUid: String): Boolean
    suspend fun getConnections(token: String): Result<List<Connection>>
}
