package com.harera.insta.di

import com.harera.repository.db.network.abstract_.*
import com.harera.repository.db.network.firebase.*
import org.koin.dsl.module

val RepoModule = module {

    single<PostRepository> {
        FirebasePostRepository(get(), get())
    }

    single<ProfileRepository> {
        FirebaseProfileRepository(get(), get())
    }

    single<NotificationsRepository> {
        FirebaseNotificationsRepository(get())
    }

    single<ChatRepository> {
        FirebaseChatRepository(get(), get())
    }

    single<AuthManager> {
        FirebaseAuthManager(get(), get())
    }
}