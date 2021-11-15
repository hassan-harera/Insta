package com.harera.profile


sealed class HomeProfileIntent {
    object GetProfile : HomeProfileIntent()
    object GetPosts : HomeProfileIntent()
}