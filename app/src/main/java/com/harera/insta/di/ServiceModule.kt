package com.harera.insta.di

import com.harera.remote.service.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.util.*
import org.koin.dsl.module

@InternalAPI
val ServiceModule = module {
    single {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }

        }
    }

    single<AuthService> {
        AuthServiceImpl(get())
    }

    single<PostService> {
        PostServiceImpl(get())
    }

    single<ProfileService> {
        ProfileServiceImpl(get())
    }

    single<MessageService> {
        MessageServiceImpl(get())
    }
}
