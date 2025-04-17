package com.example.unmei.data.model

import com.example.unmei.domain.model.Message
import com.example.unmei.domain.model.TypeRoom
import com.google.firebase.messaging.RemoteMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class FcmMessage(
    val message: NtfMessage,
)
@Serializable
data class FcmData(
    val roomId:String,
    val typeRoom:String,

    val bodyType:String,
    val senderFullName:String? = null,
    val senderIconUrl:String? = null
)
fun RemoteMessage.toMyFcmData():FcmData {
    val jsonData= Json.encodeToString(this.data)
    return Json.decodeFromString(jsonData)
}

data class NtfMessage(
    val token: String, // токен устройства
    val notification: Notification,
    val data:FcmData
)

data class Notification(
    val title: String,
    val body: String,
    val image:String
)

