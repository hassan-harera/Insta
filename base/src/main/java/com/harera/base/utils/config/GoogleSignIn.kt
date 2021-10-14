package com.harera.base.utils.config

import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

object GoogleSignIn {

    @JvmStatic
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso: GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SecretData.default_web_client_id)
                .requestEmail()
                .build()

        return GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleApiClient(context: Context?): GoogleApiClient {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SecretData.default_web_client_id)
                .requestEmail()
                .build()

        return GoogleApiClient
                .Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }
}