package com.example.unmei.data.source

import com.example.unmei.data.source.entitys.UserEntity

class LocalDataSource(
    private val dao: Dao
) {

    suspend fun saveUser(user:UserEntity){
        return dao.insertUser(user)
    }
}