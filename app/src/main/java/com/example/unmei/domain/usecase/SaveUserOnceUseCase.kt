package com.example.unmei.domain.usecase

import android.util.Log
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveUserOnceUseCase (
    private val mainRepository: MainRepository
){
    suspend fun execute(user: User): Flow<Resource<Boolean>> {

        val userExist = mainRepository.isUserExist(user.uid)
        Log.d(ConstansDev.TAG,"Пользователь "+userExist)
        if (!userExist){

            return mainRepository.saveUser(user)

        }else{
            return flow<Resource<Boolean>> {
                emit(Resource.Success(true))
            }
        }
    }
}