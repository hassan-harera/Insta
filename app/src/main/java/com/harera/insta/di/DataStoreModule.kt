package com.harera.insta.di

import com.harera.base.datastore.LocalStore
import org.koin.dsl.module


val dataStoreModule = module {

    single<LocalStore> {
        LocalStore(get())
    }

}