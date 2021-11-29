package com.harera.insta.di

import android.content.Intent
import com.harera.base.datastore.LocalStore
import com.harera.login.RefreshTokenService
import com.harera.remote.service.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.observer.*
import io.ktor.http.*
import io.ktor.util.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@InternalAPI
val serviceModule = module {

    single {
        val localStore: LocalStore by inject()

        HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            this.defaultRequest {
                headers.append(HttpHeaders.Authorization, "Bearer ${localStore.getToken()}")
            }
            ResponseObserver {
                when (it.status) {
                    HttpStatusCode.Unauthorized -> {
                        androidContext().apply {
                            startService(Intent(this,
                                RefreshTokenService::class.java).apply {
                                putExtra("token",
                                    localStore.getToken())
                            })
                        }
                    }
                }
            }
        }
    }

    single<AuthenticationService> { AuthenticationServiceImpl(get()) }

    single<NotificationsService> { NotificationsServiceImpl(get()) }

    single<PostService> { PostServiceImpl(get()) }

    single<ProfileService> { ProfileServiceImpl(get()) }

    single<MessageService> { MessageServiceImpl(get()) }

    single<SearchService> { SearchServiceImpl(get()) }

}
