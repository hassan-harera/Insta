package com.harera.repository.db.network.firebase

import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.harera.repository.db.network.abstract_.AuthManager
class FirebaseAuthManager constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
) : AuthManager {

    override suspend fun login(credecitial: AuthCredential) =
        auth.signInWithCredential(credecitial)

    override suspend fun createCredential(verificationId: String, code: String) =
        PhoneAuthProvider.getCredential(verificationId, code)

    override fun getCurrentUser(): FirebaseUser? =
        auth.currentUser

    override suspend fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()
        googleSignInClient.signOut()
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Task<AuthResult> =
        auth.signInWithCredential(credential)

    override suspend fun getSignInMethods(email: String) =
        auth.fetchSignInMethodsForEmail(email)

    override suspend fun getGoogleSignInIntent() =
        googleSignInClient.signInIntent

    override suspend fun loginAnonymously(): Task<AuthResult> =
        auth.signInAnonymously()

    override suspend fun signInWithEmailAndRandomPassword(email: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, "harera")

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    override suspend fun sendEmailVerificationCode(email: String): Task<Void> =
        auth.sendPasswordResetEmail(email)
}

