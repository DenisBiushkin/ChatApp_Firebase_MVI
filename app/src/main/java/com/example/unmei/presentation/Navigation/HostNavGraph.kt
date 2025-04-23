package com.example.unmei.presentation.Navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HostNavGraph(
    navHostController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    startDestinationRoute: String
){
    AnimatedNavHost(
       navController = navHostController,
       startDestination =startDestinationRoute ,
       route = ConstansApp.ROOT_NAVIGATE_ROUTE,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec =tween(300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {it},
                animationSpec =tween(300)
            )
        },
//        popEnterTransition = {
//          //  slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
//        },
//        popExitTransition = {
//           // slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300))
//        }

    ) {
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