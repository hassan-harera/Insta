package com.harera.remote.repository

import android.graphics.Bitmap
import com.harera.model.model.FollowRequest
import com.harera.model.model.User
import com.harera.model.response.Connection
import com.harera.remote.service.ProfileService
import com.harera.repository.ProfileRepository


class ProfileRepositoryImpl(
    private val profileService : ProfileService
) : ProfileRepository {
    override fun getProfileUser(token: String?): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(token: String): Result<User> = kotlin.runCatching {
        profileService.getProfile(token)
    }

    override suspend fun getUser(token: String, username: String): Result<User> = kotlin.runCatching {
        profileService.getUser(token, username)
    }

    override suspend fun getFollowings(uid: String): List<Connection> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowers(uid: String): List<Connection> {
        TODO("Not yet implemented")
    }

    override suspend fun getMutualFollowings(
        uid: String,
        followingsList: List<String>,
    ): List<Connection> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowRequests(uid: String): List<FollowRequest> {
        TODO("Not yet implemented")
    }

    override suspend fun addFollowRequest(followRequest: FollowRequest): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun searchProfiles(searchWord: String): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addFollower(followRelation: Connection): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addFollowing(followRelation: Connection): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addProfile(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfilePicture(imageBitmap: Bitmap, uid: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfileImageUrl(uid: String, profileImageUrl: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun checkFollowRelation(followerUid: String, followingUid: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getConnections(token: String) = kotlin.runCatching {
        profileService.getConnections(token)
    }

}