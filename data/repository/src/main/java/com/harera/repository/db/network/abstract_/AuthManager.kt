package com.harera.repository.db.network.abstract_

import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

interface AuthManager {

    suspend fun login(credecitial: AuthCredential): Task<AuthResult>
    suspend fun createCredential(verificationId: String, code: String): PhoneAuthCredential
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun signOut()
    suspend fun signInWithCredential(credential: AuthCredential): Task<AuthResult>
    suspend fun getSignInMethods(email: String): Task<SignInMethodQueryResult>
    suspend fun getGoogleSignInIntent(): Intent
    suspend fun loginAnonymously(): Task<AuthResult>
    suspend fun signInWithEmailAndRandomPassword(email: String): Task<AuthResult>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult>
    suspend fun sendEmailVerificationCode(email: String): Task<Void>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult>
}