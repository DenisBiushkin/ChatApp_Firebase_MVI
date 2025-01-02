package com.example.unmei.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unmei.presentation.sign_in.SignInScreen

@Composable
fun NavGraph(
  navHostController: NavHostController
){
   NavHost(
       navController = navHostController,
       startDestination = Screens.SignIn.route
   ){
       composable(
             route = Screens.SignIn.route
       ){
           SignInScreen(navController = navHostController)
       }
       composable(route = Screens.Main.route){
            TODO()
       }
   }
}