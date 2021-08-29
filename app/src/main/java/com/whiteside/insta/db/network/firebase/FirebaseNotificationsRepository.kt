package com.whiteside.insta.db.network.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.whiteside.insta.common.Constansts.FOLLOW_REQUESTS
import com.whiteside.insta.common.Constansts.LIKES
import com.whiteside.insta.common.Constansts.NOTIFICATIONS
import com.whiteside.insta.db.network.abstract_.NotificationsRepository
import com.whiteside.insta.modelset.FollowRequest
import javax.inject.Inject

class FirebaseNotificationsRepository @Inject constructor(
    val fStore: FirebaseFirestore,
) : NotificationsRepository {

    override fun getFriendRequests(uid: String): Task<QuerySnapshot> =
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