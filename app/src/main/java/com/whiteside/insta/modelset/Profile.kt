package com.whiteside.insta.modelset

data class Profile(
    var name: String,
    var email: String,
    var bio: String,
    var uid: String,
    var profileImageUrl: String? = null,
)