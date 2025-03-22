package com.example.unmei.domain.usecase

import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserStatusByIdUseCase (
    private val repository: MainRepository
) {

    fun execute(userId:String): Flow<StatusUser>{
        return repository.observeStatusUserById(userId)
    }

}