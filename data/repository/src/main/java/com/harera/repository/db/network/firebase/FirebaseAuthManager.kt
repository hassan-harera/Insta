package com.harera.repository.db.network.firebase

import android.app.Application
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.harera.base.utils.config.GoogleSignIn
import com.harera.repository.db.network.abstract_.AuthManager
import javax.inject.Inject

class FirebaseAuthManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val application: Application
) : AuthManager {

    private val googleSignInClient: GoogleSignInClient =
        GoogleSignIn.getGoogleSignInClient(context = application.baseContext)

    override fun login(credecitial: AuthCredential) =
        auth.signInWithCredential(credecitial)

    override fun createCredential(verificationId: String, code: String) =
        PhoneAuthProvider.getCredential(verificationId, code)

    override fun getCurrentUser(): FirebaseUser? =
        auth.currentUser

    override fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()
        googleSignInClient.signOut()
    }

    override fun signInWithCredential(credential: AuthCredential): Task<AuthResult> =
        auth.signInWithCredential(credential)

    override fun getSignInMethods(email: String) =
        auth.fetchSignInMethodsForEmail(email)

    override fun getGoogleSignInIntent() =
        googleSignInClient.signInIntent

    override fun loginAnonymously(): Task<AuthResult> =
        auth.signInAnonymously()

    override fun signInWithEmailAndRandomPassword(email: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, "harera")
}