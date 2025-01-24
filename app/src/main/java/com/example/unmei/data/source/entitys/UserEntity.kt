package com.example.unmei.data.source.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val name:String,
    val secondName:String,
    val age:Int,
)