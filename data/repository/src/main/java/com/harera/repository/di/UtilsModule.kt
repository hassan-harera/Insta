package com.harera.repository.di

import android.app.Application
import com.harera.base.utils.connection.Connectivity
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
