package com.harera.insta.di

import com.harera.remote.repository.*
import com.harera.repository.*
import org.koin.dsl.module

val RepoModule = module {

    single<AuthManager> { AuthRepositoryImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single<PostRepository> { PostRepositoryImpl(get()) }

    single<ChatRepository> { ChatRepositoryImpl(get()) }

    single<NotificationsRepository> { NotificationsRepositoryImpl(get()) }

    single<SearchRepository> { SearchRepositoryImpl(get()) }
}