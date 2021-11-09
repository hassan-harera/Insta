package com.harera.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.harera.base.base.BaseViewModel
import com.harera.base.datastore.LocalStore
import com.harera.base.state.State
import com.harera.model.model.Comment
import com.harera.model.model.FollowRequest
import com.harera.model.model.Like
import com.harera.repository.NotificationsRepository
import com.harera.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository,
    private val profileRepository: ProfileRepository,
    userSharedPreferences: LocalStore,
) : BaseViewModel<State>(userSharedPreferences) {

    private val _likeNotifications = MutableLiveData<List<Like>>()
    val likeNotifications: LiveData<List<Like>> = _likeNotifications

    private val _commentNotifications = MutableLiveData<List<Comment>>()
    val commentNotifications: LiveData<List<Comment>> = _commentNotifications

    private val _followRequestNotifications = MutableLiveData<List<Like>>()
    val followRequestNotifications: LiveData<List<Like>> = _followRequestNotifications

    init {
        getLikeNotifications()
        getCommentNotifications()
        getFollowRequestNotifications()
    }

    fun getLikeNotifications() {
        viewModelScope.async(Dispatchers.IO) {
            val task =
                notificationsRepository
                    .getLikes(token!!)
            val result = Tasks.await(task)
            result.toObjects(Like::class.java)
        }
    }

    fun getCommentNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = notificationsRepository
                .getFollowRequests(token!!)
            val result = Tasks.await(task)
            result.toObjects(Comment::class.java)
        }
    }

    private fun getFollowRequestNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val followRequestNotifications = async(Dispatchers.IO) {
                val task =
                    notificationsRepository
                        .getFollowRequests(token!!)
                val result = Tasks.await(task)
                result.toObjects(FollowRequest::class.java)
            }
        }
    }
}