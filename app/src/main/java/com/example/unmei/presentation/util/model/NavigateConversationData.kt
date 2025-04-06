package com.example.unmei.presentation.util.model

import kotlinx.serialization.Serializable
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
    fun toJson():String{
        return URLEncoder.encode(Json.encodeToString(this), "UTF-8")
    }
    companion object{

        fun fromJson(stringUrlJson:String):NavigateConversationData{
            val jsonString = URLDecoder.decode(stringUrlJson, "UTF-8")
            return Json.decodeFromString(jsonString)
        }
    }

}