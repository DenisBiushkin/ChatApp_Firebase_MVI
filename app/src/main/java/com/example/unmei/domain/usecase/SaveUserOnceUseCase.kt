package com.example.unmei.domain.usecase

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveUserOnceUseCase (
    private val mainRepository: MainRepository
){
    suspend fun execute(user: User): Flow<Resource<Boolean>> {

        val userExist = mainRepository.isUserExist(user.uid)
        if (!userExist){
            return mainRepository.saveUser(user)
        }else{
            return flow<Resource<Boolean>> {
                Resource.Success(true)
            }
        }
    }
}