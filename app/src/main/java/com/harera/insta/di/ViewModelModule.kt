package com.harera.insta.di

import com.harera.add_chat.NewChatViewModel
import com.harera.base.base.LocalStoreViewModel
import com.harera.chat.ChatViewModel
import com.harera.feed.FeedViewModel
import com.harera.insta.MainViewModel
import com.harera.login.LoginViewModel
import com.harera.login.SignupViewModel
import com.harera.mychats.MyChatsViewModel
import com.harera.notifications.NotificationsViewModel
import com.harera.posting.ImagePosting
import com.harera.profile.HomeProfileViewModel
import com.harera.psot.PostViewModel
import com.harera.search.SearchViewModel
import com.harera.text_post.TextPostingViewModel
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

    viewModel<LocalStoreViewModel> {
        LocalStoreViewModel(get())
    }

    viewModel {
        com.harera.post.ImagePostViewModel(get(), get(), get())
    }

    viewModel<ImagePosting> {
        ImagePosting(get(), get())
    }

    viewModel<PostViewModel> {
        PostViewModel(get(), get(), get())
    }

    viewModel<FeedViewModel> {
        FeedViewModel(get(), get(), get())
    }

    viewModel<LoginViewModel> {
        LoginViewModel(get(), get(), get())
    }


    viewModel<SignupViewModel> {
        SignupViewModel(get(), get(), get())
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

    viewModel<TextPostingViewModel> {
        TextPostingViewModel(get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }
}