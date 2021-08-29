package com.whiteside.dwaa.di

import android.app.Application
import com.whiteside.dwaa.utils.connection.Connectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    companion object {
        @Provides
        @Singleton
        fun provideConnectivity(application: Application) : Connectivity =
            Connectivity(application)
    }
}
