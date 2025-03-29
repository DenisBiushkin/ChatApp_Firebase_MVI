package com.example.unmei.presentation.Navigation

import android.util.Log
import com.example.unmei.presentation.util.model.NavigateConversationData
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev.TAG
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screens(
    val route:String
) {
    object SignIn:Screens(route="signIn_screen")

    object Main:Screens("main_screen")
    object Drawer:Screens("drawer_screen")
    object Friends:Screens(route = "friends_screen"){

    }
    object Profile:Screens(route ="profile_info/{${ConstansApp.PROFILE_ARGUMENT_JSON}}" ){
        fun withJsonData(
            userUid:String
        ): String {
            return  "profile_info/${Json.encodeToString(userUid)}"
        }
        fun fromJsonData(data:String):String{
            return Json.decodeFromString(data)
        }
    }
    object Chat:Screens("chat_screen/{${ConstansApp.CHAT_ARGUMENT_JSON}}"){


        fun withInfo(groupUid:String, companionUid:String):String{
            return "chat_screen/$groupUid/$companionUid"
        }

        fun withExistenceData(
             chatExist: Boolean = true,
             chatUrl:String,
             chatName:String,
             companionUid: String,
             chatUid: String? = null,
        ):String{
            val data = NavigateConversationData(
                chatName = chatName,
                chatExist = chatExist,
                chatUrl = chatUrl,
                companionUid = companionUid,
                chatUid = chatUid
            )

            //нельзя напрямую передать json так как он содержит ! {} ?
            return "chat_screen/${URLEncoder.encode(Json.encodeToString(data), "UTF-8")}"
        }
        fun fromJsonToExistenceData(stringUrlJson: String):NavigateConversationData{
            val jsonString = URLDecoder.decode(stringUrlJson, "UTF-8")
            return Json.decodeFromString(jsonString)
        }

    }

}