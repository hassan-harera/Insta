package com.harera.insta.di

import com.harera.base.coil.CoilLoader
import org.koin.dsl.module

val coilModule = module {

    single {
        CoilLoader(get(), get())
    }
}