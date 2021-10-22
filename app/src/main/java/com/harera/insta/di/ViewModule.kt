package com.harera.insta.di

import com.harera.chat.ChatViewModel
import com.harera.feed.FeedViewModel
import com.harera.feed.post.PostCardViewModel
import com.harera.insta.MainActivity.MainViewModel
import com.harera.login.LoginViewModel
import com.harera.notifications.NotificationsViewModel
import com.harera.posting.AddPostViewModel
import com.harera.profile.HomeProfileViewModel
import com.harera.psot.PostViewModel
import com.harera.visit_profile.VisitProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val ViewModel = module {

    viewModel {
        MainViewModel(get())
    }

    viewModel<PostCardViewModel> {
        PostCardViewModel(get(), get())
    }

    viewModel<HomeProfileViewModel> {
        HomeProfileViewModel(get(), get(), get())
    }

    viewModel<VisitProfileViewModel> {
        VisitProfileViewModel(get(), get(), get())
    }


    viewModel<AddPostViewModel> {
        AddPostViewModel(get(), get(), get())
    }

    viewModel<PostViewModel> {
        PostViewModel(get(), get(), get())
    }

    viewModel<NotificationsViewModel> {
        NotificationsViewModel(get(), get())
    }

    viewModel<ChatViewModel> {
        ChatViewModel(get(), get(), get())
    }

    viewModel<FeedViewModel> {
        FeedViewModel(get(), get(), get())
    }

    viewModel<LoginViewModel> {
        LoginViewModel(get())
    }
}