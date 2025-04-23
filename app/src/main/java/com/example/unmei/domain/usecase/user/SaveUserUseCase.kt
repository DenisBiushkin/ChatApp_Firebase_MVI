package com.example.unmei.domain.usecase.user

import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

class SaveUserUseCase (
    private val mainRepository: MainRepository
){
  suspend fun execute(user: User): Resource<Unit> {
      return mainRepository.saveUser(user)
  }
}