package com.example.unmei.presentation.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.unmei.presentation.sign_in_feature.components.MainScreen
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
){
    navigation(
        startDestination = Screens.Main.route,
        route = ConstansApp.MAIN_NAVIGATE_ROUTE
    ){
        composable(route = Screens.Main.route){
            MainScreen(navController=navController)
        }
    }
}