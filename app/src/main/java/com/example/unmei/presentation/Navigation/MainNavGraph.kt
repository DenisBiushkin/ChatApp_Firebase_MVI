package com.example.unmei.presentation.Navigation

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.unmei.presentation.conversation_future.components.ChatScreen
import com.example.unmei.presentation.chat_list_feature.components.DrawerScreen
import com.example.unmei.presentation.chat_list_feature.components.MainScreen
import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansDev

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
){
    navigation(
        startDestination = Screens.Drawer.route,
        route = ConstansApp.MAIN_NAVIGATE_ROUTE
    ){
        composable(route = Screens.Drawer.route){
          //  MainScreen(navController=navController)
            DrawerScreen(navController = navController, googleAuthUiClient = googleAuthUiClient)
        }
        composable(route = Screens.Main.route){
            MainScreen(navController=navController)
        }
        composable(
            route=Screens.Chat.route,
            arguments = listOf(
                navArgument(
                    name = ConstansApp.CHAT_ARGUMENT_JSON,
                ){
                  type = NavType.StringType
                },

            )
        ){
            //переделать, чтобы засунуть данные сразу в конструктор ViewModel
            val viewModel = hiltViewModel<ConversationViewModel>()

            val jsonData = it.arguments!!.getString(ConstansApp.CHAT_ARGUMENT_JSON)!!
            val exsitenceData = Screens.Chat.fromJsonToExistenceData(jsonData)

            Log.d(ConstansDev.TAG,"Receive Arguments $exsitenceData")

            viewModel.saveNecessaryInfo(exsitenceData)
            ChatScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}