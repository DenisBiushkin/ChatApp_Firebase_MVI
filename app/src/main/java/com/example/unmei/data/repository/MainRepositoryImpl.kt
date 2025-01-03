package com.example.unmei.data.repository

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.ConstansApp.USERS_REFERENCE_DB
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.Resource
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MainRepositoryImpl():MainRepository {
    override fun saveUser(user: User): Flow<Resource<Boolean>> {
        val db= FirebaseDatabase.getInstance(ConstansDev.YOUR_URL_DB)
        val ref = db.getReference(USERS_REFERENCE_DB)

        return flow {
            try{
                emit(Resource.Loading())
                ref.child(user.uid).setValue(user).await()
                emit(Resource.Success(data = true))
            }catch(e:Exception){
                emit(Resource.Error(message = e.toString()))
            }
        }
    }
}