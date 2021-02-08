package com.whiteside.insta.ui;

import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.whiteside.insta.R;

public class GoogleSignIn {


    private static GoogleSignInOptions gso;

    public static GoogleSignInClient getGoogleSignInClient(Context context) {
        if (gso == null) {
            gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        }
        return com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso);
    }

    public static GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient
                .Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

}
