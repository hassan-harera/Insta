package com.harera.repository.db.network.firebase

import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.harera.repository.db.network.abstract_.AuthManager

class FakeAuthManager :AuthManager {

    override suspend fun login(credecitial: AuthCredential): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun createCredential(
        verificationId: String,
        code: String
    ): PhoneAuthCredential {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): FirebaseUser? =
        FirebaseAuth.getInstance().currentUser

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getSignInMethods(email: String): Task<SignInMethodQueryResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getGoogleSignInIntent(): Intent {
        TODO("Not yet implemented")
    }

    override suspend fun loginAnonymously(): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithEmailAndRandomPassword(email: String): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun sendEmailVerificationCode(email: String): Task<Void> {
        TODO("Not yet implemented")
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Task<AuthResult> {
        TODO("Not yet implemented")
    }
}