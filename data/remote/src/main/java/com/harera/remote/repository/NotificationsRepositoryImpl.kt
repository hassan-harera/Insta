package com.harera.remote.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.harera.model.model.FollowRequest
import com.harera.repository.NotificationsRepository

class NotificationsRepositoryImpl : NotificationsRepository {
    override suspend fun getFollowRequests(uid: String): Task<QuerySnapshot> {
        TODO("Not yet implemented")
    }

    override suspend fun getLikes(uid: String): Task<QuerySnapshot> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriendRequest(followRequest: FollowRequest): Task<Void> {
        TODO("Not yet implemented")
    }
}
