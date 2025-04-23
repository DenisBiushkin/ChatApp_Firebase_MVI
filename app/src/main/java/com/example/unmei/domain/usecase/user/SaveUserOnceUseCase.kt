package com.example.unmei.domain.usecase.user

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
    suspend fun execute(user: User): Resource<Unit>{

        val userExist = mainRepository.isUserExist(user.uid)
        //Log.d(ConstansDev.TAG,"Пользователь "+userExist)
        if (!userExist){
            return mainRepository.saveUser(user)
        }else{
            return Resource.Success(Unit)

        }
    }
}