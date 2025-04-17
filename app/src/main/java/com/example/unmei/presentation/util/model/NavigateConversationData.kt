package com.example.unmei.presentation.util.model

import android.util.Log
import com.example.unmei.util.ConstansDev.TAG
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@Serializable
data class NavigateConversationData(
    val chatExist: Boolean = true,
    val chatUrl:String,
    val chatName:String,
    val companionUid: String,
    val chatUid: String? = null,
){

//    fun toJson():String{
//        return URLEncoder.encode(Json.encodeToString(this), "UTF-8")
//    }
//    companion object{
//
//        fun fromJson(stringUrlJson:String):NavigateConversationData{
//            val jsonString = URLDecoder.decode(stringUrlJson, "UTF-8")
//            return Json.decodeFromString(jsonString)
//        }
//    }
    fun toJson(): String {
        val encodedChatUrl = URLEncoder.encode(this.chatUrl,ENC)
        val jsonPart = Json.encodeToString(
            this.copy(chatUrl = encodedChatUrl)
        )
        return URLEncoder.encode(jsonPart, ENC)
    }

    companion object {
        private val ENC="UTF-8"
        fun fromJson(encoded: String): NavigateConversationData {
            val jsonString = URLDecoder.decode(encoded, ENC)
            val data = Json.decodeFromString<NavigateConversationData>(jsonString)
            Log.d(TAG,"URL There $data")
            return data
        }
    }

}