package com.harera.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.harera.model.modelget.Comment
import com.harera.model.modelget.FollowRequest
import com.harera.model.modelget.Like
import com.harera.repository.db.network.abstract_.AuthManager
import com.harera.repository.db.network.abstract_.NotificationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationsRepository: NotificationsRepository,
    private val authManager: AuthManager,
) : ViewModel() {

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
            val task = notificationsRepository.getLikes(authManager.getCurrentUser()!!.uid)
            val result = Tasks.await(task)
            result.toObjects(Like::class.java)
        }
    }

    fun getCommentNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = notificationsRepository.getFollowRequests(authManager.getCurrentUser()!!.uid)
            val result = Tasks.await(task)
            result.toObjects(Comment::class.java)
        }
    }

    fun getFollowRequestNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val followRequestNotifications = async(Dispatchers.IO) {
                val task =
                    notificationsRepository.getFollowRequests(authManager.getCurrentUser()!!.uid)
                val result = Tasks.await(task)
                result.toObjects(FollowRequest::class.java)
            }
        }
    }
}