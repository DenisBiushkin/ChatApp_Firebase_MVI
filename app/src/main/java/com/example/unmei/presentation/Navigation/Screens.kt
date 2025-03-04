package com.example.unmei.presentation.Navigation

import com.example.unmei.util.ConstansApp

sealed class Screens(
    val route:String
) {
    object SignIn:Screens(route="signIn_screen")

    object Main:Screens("main_screen")
    object Drawer:Screens("drawer_screen")
    object Chat:Screens("chat_screen/{${ConstansApp.CHAT_AGUIMENT_GROUPUID}}/{${ConstansApp.CHAT_ARGUMENT_COMPANIONUID}}"){
        fun withInfo(groupUid:String, companionUid:String):String{
            return "chat_screen/$groupUid/$companionUid"
        }
    }

}