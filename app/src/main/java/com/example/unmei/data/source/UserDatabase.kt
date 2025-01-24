package com.example.unmei.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unmei.data.source.entitys.UserEntity


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase:RoomDatabase() {
    abstract val dao:Dao
    companion object{
        val Db_name="Fisrt version DB"
    }
}