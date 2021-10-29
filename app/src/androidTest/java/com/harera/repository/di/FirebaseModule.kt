package com.harera.repository.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val FirebaseModule = module {

    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }

    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }

    single<FirebaseDatabase> {
        FirebaseDatabase.getInstance()
    }

    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }
}