package com.harera.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.harera.model.model.FollowRequest

interface NotificationsRepository {

    suspend fun getFollowRequests(uid: String): Task<QuerySnapshot>
    suspend fun getLikes(uid: String): Task<QuerySnapshot>
    suspend fun removeFriendRequest(followRequest: FollowRequest): Task<Void>
}
