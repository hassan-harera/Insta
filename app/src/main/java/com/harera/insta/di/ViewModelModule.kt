package com.harera.insta.di

import com.harera.add_chat.NewChatViewModel
import com.harera.chat.ChatViewModel
import com.harera.feed.FeedViewModel
import com.harera.insta.MainViewModel
import com.harera.login.LoginViewModel
import com.harera.mychats.MyChatsViewModel
import com.harera.notifications.NotificationsViewModel
import com.harera.posting.PostingViewModel
import com.harera.profile.HomeProfileViewModel
import com.harera.psot.PostViewModel
import com.harera.visit_profile.VisitProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val ViewModel = module {

    viewModel {
        MainViewModel(get())
    }

    viewModel<HomeProfileViewModel> {
        HomeProfileViewModel(get(), get(), get())
    }

    viewModel<VisitProfileViewModel> {
        VisitProfileViewModel(get(), get(), get())
    }

    viewModel<com.harera.post.PostViewModel> {
        com.harera.post.PostViewModel(get(), get(), get())
    }

    viewModel<PostingViewModel> {
        PostingViewModel(get(), get(), get())
    }

    viewModel<PostViewModel> {
        PostViewModel(get(), get(), get())
    }

    viewModel<FeedViewModel> {
        FeedViewModel(get(), get(), get())
    }

    viewModel<LoginViewModel> {
        LoginViewModel(get())
    }

    viewModel<NotificationsViewModel> {
        NotificationsViewModel(get(), get())
    }

    viewModel<ChatViewModel> {
        ChatViewModel(get(), get(), get())
    }

    viewModel<MyChatsViewModel> {
        MyChatsViewModel(get(), get(), get())
    }

    viewModel<NewChatViewModel> {
        NewChatViewModel(get(), get())
    }
}