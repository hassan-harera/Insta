package com.harera.login.di

import com.harera.login.SocialLoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {

    viewModel {
        SocialLoginViewModel(get(), get())
    }
}