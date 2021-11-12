package com.harera.insta.di

import com.harera.remote.repository.AuthRepositoryImpl
import com.harera.remote.repository.ChatRepositoryImpl
import com.harera.remote.repository.PostRepositoryImpl
import com.harera.remote.repository.ProfileRepositoryImpl
import com.harera.repository.AuthManager
import com.harera.repository.ChatRepository
import com.harera.repository.PostRepository
import com.harera.repository.ProfileRepository
import org.koin.dsl.module

val RepoModule = module {

    single<AuthManager> {
        AuthRepositoryImpl(get())
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(get())
    }

    single<PostRepository> {
        PostRepositoryImpl(get())
    }

    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }
}