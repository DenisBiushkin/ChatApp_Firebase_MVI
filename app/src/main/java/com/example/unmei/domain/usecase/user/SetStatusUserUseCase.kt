package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.StatusUser
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource

class SetStatusUserUseCase(
    private val repository: MainRepository
) {

    suspend fun execute(userId:String,status: StatusUser): Resource<String>{
        return repository.setStatusUser(userId,status)
    }
}