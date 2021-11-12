//package com.harera.insta.di
//
//import androidx.room.Room
//import com.harera.local.InstaDatabase
//import org.koin.dsl.module
//
//val dbModule = module {
//    single<InstaDatabase> {
//        Room.databaseBuilder(
//            get(),
//            InstaDatabase::class.java, "database-name"
//        ).build()
//    }
//
//    single {
//        get<InstaDatabase>().getDao()
//    }
//}