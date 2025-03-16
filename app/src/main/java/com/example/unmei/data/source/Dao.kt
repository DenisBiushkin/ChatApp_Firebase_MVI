package com.example.unmei.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.unmei.data.source.entitys.UserEntity

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

//    @Query("SELECT * from user_table")
//    fun getAllUsers(): Flow<List<User>>

}