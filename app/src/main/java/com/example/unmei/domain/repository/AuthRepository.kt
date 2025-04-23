package com.example.unmei.domain.repository

import com.example.unmei.util.Resource

interface AuthRepository {

    suspend fun registerWithEmail(email: String, password: String,fullName:String): Resource<Unit>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Unit>

    fun signOutEmail()
}