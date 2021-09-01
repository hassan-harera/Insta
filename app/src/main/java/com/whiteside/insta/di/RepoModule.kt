package com.whiteside.insta.di

import com.whiteside.insta.db.network.abstract_.*
import com.whiteside.insta.db.network.firebase.*
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