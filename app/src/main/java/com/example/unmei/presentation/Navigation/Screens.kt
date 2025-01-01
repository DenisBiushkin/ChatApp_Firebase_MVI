package com.example.unmei.presentation.Navigation

sealed class Screens(
    val route:String
) {
    object SignIn:Screens(route="signIn_screen")

    object Main:Screens("main_screen")
}