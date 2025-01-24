package com.example.unmei.data.repository

import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.source.LocalDataSource
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

class MainRepositoryImpl(
   private val localDataSource: LocalDataSource, //  Room
    private val remoteDataSource: RemoteDataSource // Firebase
):MainRepository {


    override suspend fun isUserExist(userId:String):Boolean{
        return remoteDataSource.isUserExists(userId)
    }

    override fun saveUser(user: User): Flow<Resource<Boolean>> {
        return flow {
            try{
                emit(Resource.Loading())

                remoteDataSource.saveUserData(user=user)

                emit(Resource.Success(data = true))
            }catch(e:Exception){
                emit(Resource.Error(message = e.toString()))
            }
        }
    }
}