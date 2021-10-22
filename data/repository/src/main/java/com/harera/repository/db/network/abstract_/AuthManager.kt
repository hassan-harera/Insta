package com.harera.repository.db.network.abstract_

import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

interface AuthManager {

    fun login(credecitial: AuthCredential): Task<AuthResult>
    fun createCredential(verificationId: String, code: String): PhoneAuthCredential
    fun getCurrentUser(): FirebaseUser?
    fun signOut()
    fun signInWithCredential(credential: AuthCredential): Task<AuthResult>
    fun getSignInMethods(email: String): Task<SignInMethodQueryResult>
    fun getGoogleSignInIntent(): Intent
    fun loginAnonymously(): Task<AuthResult>
    fun signInWithEmailAndRandomPassword(email: String): Task<AuthResult>
    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult>
    fun sendEmailVerificationCode(email: String): Task<Void>
    fun signUpWithEmailAndPassword(email: String, password: String): Task<AuthResult>
}