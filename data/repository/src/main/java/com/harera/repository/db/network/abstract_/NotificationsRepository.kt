package com.harera.repository.db.network.abstract_

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.harera.model.modelset.FollowRequest

interface NotificationsRepository {

    fun getFollowRequests(uid: String): Task<QuerySnapshot>
    fun getLikes(uid: String): Task<QuerySnapshot>
    fun removeFriendRequest(followRequest: FollowRequest): Task<Void>
}
