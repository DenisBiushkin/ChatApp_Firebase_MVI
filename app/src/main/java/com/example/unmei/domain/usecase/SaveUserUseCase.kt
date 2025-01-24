package com.example.unmei.domain.usecase

import com.example.unmei.data.repository.MainRepositoryImpl
import com.example.unmei.domain.model.User
import com.example.unmei.domain.repository.MainRepository
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

class SaveUserUseCase (
    private val mainRepository: MainRepository
){
  fun execute(user: User): Flow<Resource<Boolean>> {
      return mainRepository.saveUser(user)
  }
}