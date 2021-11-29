package com.harera.remote

object Routing {
    const val INSERT_MESSAGE = "${URL.BASE_URL}/message"
    const val GET_PROFILE_POSTS = "${URL.BASE_URL}/profile/posts"
    const val GET_CONNECTIONS = "${URL.BASE_URL}/connections"
    const val POST = "/posts/"
    const val SIGNUP = "/signup"
    const val LOGIN = "/login"
    const val USER = "/User"
    const val NOTIFICATIONS = "/notifications"
    const val FEED = "/feed"
    const val GET_PROFILE = "${URL.BASE_URL}/profile"
}

object Parameters {
    const val ACCESS_TOKEN: String = "access_token"
    const val UID: String = "uid"
    const val Login_Method = "method"
    const val TOKEN = "token"
    const val Signup_Method = "method"
    const val USERNAME = "username"
    const val IMAGE_NAME = "image_name"
    const val PID = "pid"
}

object LoginMethods {
    const val EMAIL = "email"
    const val FACEBOOK = "facebook"
    const val Google = "google"
}

object AuthenticationParameters {
    const val USERNAME = "username"
    const val BEARER = "Bearer"
}

object Constants {
    const val TOKEN = "token"
}

object URL {
    const val BASE_URL = "http://192.168.1.15:5000/"
}