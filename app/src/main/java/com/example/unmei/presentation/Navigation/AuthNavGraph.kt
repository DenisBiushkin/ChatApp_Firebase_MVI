package com.example.unmei.presentation.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.unmei.presentation.registration_feature.components.RegistrationScreen
import com.example.unmei.presentation.sign_in_feature.components.LoginInScreenFull
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
){
    navigation(
        startDestination = Screens.SignIn.route,
        route = ConstansApp.AUTH_NAVIGATE_ROUTE
    ){
        composable(
            route = Screens.SignIn.route
        ){
            LoginInScreenFull(
                navController = navController,
                googleAuthUiClient =  googleAuthUiClient
            )
        }
        composable(
            route = Screens.Registration.route
        ){
            RegistrationScreen(
                navController = navController
            )

        }
    }
}