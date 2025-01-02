package com.example.unmei.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unmei.presentation.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.sign_in.LoginInScreenFull
import com.example.unmei.presentation.sign_in.SignInScreen

@Composable
fun NavGraph(
  navHostController: NavHostController,
  googleAuthUiClient: GoogleAuthUiClient
){
   NavHost(
       navController = navHostController,
       startDestination = Screens.SignIn.route
   ){
       composable(
             route = Screens.SignIn.route
       ){
           LoginInScreenFull(
               navController = navHostController,
               googleAuthUiClient =  googleAuthUiClient
           )
       }
       composable(route = Screens.Main.route){
            TODO()
       }
   }
}