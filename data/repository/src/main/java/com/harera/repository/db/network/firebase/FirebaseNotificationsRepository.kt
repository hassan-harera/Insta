package com.harera.repository.db.network.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.harera.model.modelset.FollowRequest
import com.harera.repository.common.Constansts.FOLLOW_REQUESTS
import com.harera.repository.common.Constansts.LIKES
import com.harera.repository.common.Constansts.NOTIFICATIONS
import com.harera.repository.db.network.abstract_.NotificationsRepository

class FirebaseNotificationsRepository constructor(
    private val fStore: FirebaseFirestore,
) : NotificationsRepository {

    override fun getFollowRequests(uid: String): Task<QuerySnapshot> =
        fStore
            .collection(NOTIFICATIONS)
            .document(uid)
            .collection(FOLLOW_REQUESTS)
            .get()

    override fun getLikes(uid: String): Task<QuerySnapshot> =
        fStore
            .collection(NOTIFICATIONS)
            .document(uid)
            .collection(LIKES)
            .get()

    override fun removeFriendRequest(followRequest: FollowRequest): Task<Void> =
        fStore
            .collection(NOTIFICATIONS)
            .document(followRequest.toUid)
            .collection(FOLLOW_REQUESTS)
            .document(followRequest.id!!)
            .delete()
}