package com.example.unmei.presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp

@Composable
fun HostNavGraph(
    navHostController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    startDestinationRoute: String
){
   NavHost(
       navController = navHostController,
       startDestination =startDestinationRoute ,
       route = ConstansApp.ROOT_NAVIGATE_ROUTE

   ){
       authNavGraph(
           navController = navHostController,
           googleAuthUiClient = googleAuthUiClient
       )
       mainNavGraph(
           navController = navHostController,
           googleAuthUiClient = googleAuthUiClient
       )

   }
}