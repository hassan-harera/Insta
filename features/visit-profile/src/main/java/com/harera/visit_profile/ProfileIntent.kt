package com.harera.visit_profile


sealed class ProfileIntent {
    object GetProfile : ProfileIntent()
    object GetPosts : ProfileIntent()
    object None : ProfileIntent()
}