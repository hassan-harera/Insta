package com.whiteside.insta.db.network.abstract_

import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import com.whiteside.insta.modelset.FollowRelation
import com.whiteside.insta.modelset.FollowRequest
import com.whiteside.insta.modelset.Profile

interface ProfileRepository {

    fun addFollowRequest(followRequest: FollowRequest): Task<Void>
    fun searchProfiles(searchWord: String): Task<QuerySnapshot>
    fun updateProfile(profile: Profile): Task<Void>
    fun addFollower(followRelation: FollowRelation): Task<Void>
    fun addFollowing(followRelation: FollowRelation): Task<Void>
    fun getFollowings(uid: String): Task<QuerySnapshot>
    fun addProfile(profile: Profile): Task<Void>
    fun getFollowRequests(uid: String): Task<QuerySnapshot>
    fun getFollowers(uid: String): Task<QuerySnapshot>
    fun getProfile(uid: String): Task<DocumentSnapshot>
    fun updateProfilePicture(imageBitmap: Bitmap, uid: String): UploadTask
    fun updateProfileImageUrl(uid: String, profileImageUrl: String): Task<Void>
    fun checkFollowRelation(followerUid: String, followingUid: String): Task<QuerySnapshot>
    fun getMutualFollowings(uid: String, followingsList: List<String>): Task<QuerySnapshot>
    fun getFollower(followingUid: String, followerUid: String): Task<QuerySnapshot>
}
