package com.example.unmei.data.repository

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.AuthRepository
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
   private val mainRepository: MainRepository
) :AuthRepository{

    private val auth= Firebase.auth

    override suspend fun registerWithEmail(email: String, password: String,fullName:String): Resource<Unit> {
        return try {
            val data=auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val newUser= User(
                uid = data.user!!.uid,
                fullName = fullName,
                userName = fullName
            )
            mainRepository.saveUser(newUser)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(message = e.toString())
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<Unit> {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            return Resource.Success(Unit)
        }catch (e:Exception){
            return Resource.Error(message = e.toString())
        }
    }

    override fun signOutEmail() {
        auth.signOut()
    }
}