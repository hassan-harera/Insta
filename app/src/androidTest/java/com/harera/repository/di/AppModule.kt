package com.harera.repository.di

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.harera.repository.db.network.config.GoogleSignIn
import org.koin.dsl.module

val AppModule = module{

    single<GoogleSignInClient> {
        GoogleSignIn.getGoogleSignInClient(get())
    }
}