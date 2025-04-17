package com.example.unmei.data.model

import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.TypeRoom
import com.google.firebase.messaging.RemoteMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
@Serializable
data class FcmMessage(
    val message: NtfMessage,
){
    fun toJson():String{
        return Json.encodeToString(this)
    }
    companion object{
        fun fromJson(jsonFcmMessage:String):FcmMessage{
            return Json.decodeFromString(jsonFcmMessage)
        }
    }

}
@Serializable
data class FcmData(
    val roomId:String,
    val typeRoom:String,

    val title: String,
    val body: String,
    val image:String? = null,

    val bodyType:String,
    val senderFullName:String? = null,
    val senderIconUrl:String? = null
)
fun RemoteMessage.toMyFcmData():FcmData {
    //Map<String,String> -> Json Data -> Data class
    val jsonData= Json.encodeToString(this.data)
    return Json.decodeFromString(jsonData)
}

@Serializable
data class NtfMessage(
    val token: String="", // токен устройства
    val notification: Notification?=null,//не использовать т.к payload и onReceivedMessage не срабатывает
    val data:FcmData
)
@Serializable
data class Notification(
    val title: String,
    val body: String,
    val image:String? = null
)

