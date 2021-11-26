package com.harera.base.coil

import android.content.Context
import coil.request.ImageRequest
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers

object CoilUtils {

    fun createRequest(imageUrl: String?, context: Context, token: String) =
      ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .dispatcher(Dispatchers.IO)
            .addHeader(HttpHeaders.Authorization, "Bearer $token")
            .build()
}