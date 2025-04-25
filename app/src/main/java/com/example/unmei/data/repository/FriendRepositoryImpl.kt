package com.example.unmei.data.repository

import com.example.unmei.data.network.RemoteDataSource
import com.example.unmei.data.source.LocalDataSource
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.domain.repository.FriendRepository

class FriendRepositoryImpl(
    private val localDataSource: LocalDataSource, //  Room
    private val remoteDataSource: RemoteDataSource,// Firebase,
): FriendRepository {

    override suspend fun getFriendById(userId: String): UserExtended? {
        return null
    }

    override suspend fun addFriendById(userId: String, friendId: String): Boolean {
        return remoteDataSource.addFriendByIdRemote(userId,friendId)
    }

    override suspend fun deleteFriendById(userId: String, friendId: String): Boolean {
        return remoteDataSource.deleteFriendByIdRemote(userId,friendId)
    }
}