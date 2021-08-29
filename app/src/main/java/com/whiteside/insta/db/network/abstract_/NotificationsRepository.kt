package com.whiteside.insta.db.network.abstract_

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.whiteside.insta.modelset.FollowRequest

interface NotificationsRepository {

    fun getFriendRequests(uid: String): Task<QuerySnapshot>
    fun getLikes(uid: String): Task<QuerySnapshot>
    fun removeFriendRequest(followRequest: FollowRequest): Task<Void>
}
