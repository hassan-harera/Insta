package com.whiteside.dwaa.di

import com.whiteside.dwaa.network.repository.firebase.FirebaseChatRepository
import com.whiteside.insta.db.network.abstract_.*
import com.whiteside.insta.db.network.firebase.FirebaseAuthManager
import com.whiteside.insta.db.network.firebase.FirebaseNotificationsRepository
import com.whiteside.insta.db.network.firebase.FirebasePostRepository
import com.whiteside.insta.db.network.firebase.FirebaseProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindChatRepository(firebaseChatRepository: FirebaseChatRepository): ChatRepository

    @Binds
    abstract fun bindPostRepository(firebasePostRepository: FirebasePostRepository): PostRepository

    @Binds
    abstract fun bindAuthManager(firebaseAuthManager: FirebaseAuthManager): AuthManager

    @Binds
    abstract fun bindProfileRepository(firebaseProfileRepository: FirebaseProfileRepository): ProfileRepository

    @Binds
    abstract fun bindNotificationsRepository(firebaseNotificationsRepository: FirebaseNotificationsRepository): NotificationsRepository
}