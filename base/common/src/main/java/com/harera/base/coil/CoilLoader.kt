package com.harera.base.coil

import android.content.Context
import coil.request.ImageRequest
import com.harera.base.R
import com.harera.base.datastore.LocalStore
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers

class CoilLoader(
    private val localStore: LocalStore,
    private val context: Context,
) {
    fun imageRequest(imageUrl: String?) =
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .dispatcher(Dispatchers.IO)
            .placeholder(R.drawable.insta)
            .crossfade(true)
            .error(R.drawable.insta)
            .fallback(R.drawable.insta)
            .addHeader(HttpHeaders.Authorization, "Bearer ${localStore.getToken()}")
            .build()
}