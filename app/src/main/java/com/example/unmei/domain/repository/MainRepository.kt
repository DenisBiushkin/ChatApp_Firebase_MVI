package com.example.unmei.domain.repository

import com.example.unmei.domain.model.User
import com.example.unmei.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainRepository {


    fun saveUser(user: User): Flow<Resource<Boolean>>
}