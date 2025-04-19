package com.example.unmei.android_frameworks.receiver.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NotificationDataExtra(
    val notificationId:Int,//нужен для ответа или отмены уведомления
    val roomId:String?=null
){
    companion object{
        fun fromJson(jsonExtras:String): NotificationDataExtra {
            return Json.decodeFromString<NotificationDataExtra>(jsonExtras)
        }
    }

    fun toJson():String{
        return Json.encodeToString(this)
    }
}