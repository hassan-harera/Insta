package com.harera.profile


sealed class ProfileIntent {
    object GetProfile : ProfileIntent()
    object GetPosts : ProfileIntent()
    object None : ProfileIntent()
}