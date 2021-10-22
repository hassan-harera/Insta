package com.harera.insta.di

import com.harera.base.utils.connection.Connectivity
import org.koin.dsl.module

val UtilsModule = module {

    single {
        Connectivity(get())
    }
}
