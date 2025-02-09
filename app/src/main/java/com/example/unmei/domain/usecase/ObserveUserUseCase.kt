package com.example.unmei.domain.usecase

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class ObserveUserUseCase (
    private val mainRepository: MainRepository
){
    suspend fun execute(userId:String): Flow<User> =mainRepository.observeUser(userId)
}