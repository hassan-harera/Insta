package com.harera.repository.di

import androidx.room.Room
import com.harera.local.InstaDatabase
import org.koin.dsl.module

val  DbModule = module {
    single {
        Room.databaseBuilder(
            get(),
            InstaDatabase::class.java, "database-name"
        ).build()
    }
}