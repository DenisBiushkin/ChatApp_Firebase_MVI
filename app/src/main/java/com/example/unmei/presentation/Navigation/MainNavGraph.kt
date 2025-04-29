package com.example.unmei.presentation.Navigation

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.unmei.presentation.conversation_future.components.ChatScreen
import com.example.unmei.presentation.chat_list_feature.components.DrawerScreen

import com.example.unmei.presentation.conversation_future.viewmodel.ConversationViewModel
import com.example.unmei.presentation.create_group_feature.components.NewGroupSelectUsersScreen
import com.example.unmei.presentation.friends_feature.components.FriendsScreen
import com.example.unmei.presentation.friends_feature.viewmodel.FriendsViewModel
import com.example.unmei.presentation.profile_user_feature.components.ProfileUserFull
import com.example.unmei.presentation.profile_user_feature.viewmodel.ProfileUserViewModel
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.presentation.util.model.NavigateConversationData
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansApp.CHAT_ARGUMENT_JSON
import com.example.unmei.util.ConstansApp.CHAT_URI_DEEPLINK
import com.example.unmei.util.ConstansApp.CREATEGROUP_ARGUMENT_USERID
import com.example.unmei.util.ConstansDev
import com.example.unmei.util.ConstansDev.TAG

@RequiresApi(35)
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
           // MainScreen(navController=navController)
        }
        composable(
            route = Screens.Friends.route,

        ){

            val viewModel = hiltViewModel<FriendsViewModel>()
            FriendsScreen(
                navController= navController,
                viewModel = viewModel
            )
        }

        composable(
            route=Screens.Profile.route,
            arguments = listOf(
                navArgument(name = ConstansApp.PROFILE_ARGUMENT_JSON){
                    type = NavType.StringType
                }
            ),
        ){
            val navData= it.arguments!!.getString(ConstansApp.PROFILE_ARGUMENT_JSON)!!
            Log.d(TAG,"Profile NavArg: "+navData)

            val viewModel = hiltViewModel<ProfileUserViewModel>()
            viewModel.saveData(Screens.Profile.fromJsonData(navData))
            ProfileUserFull(
                navController = navController,
                viewmodel = viewModel
            )
        }

        composable(
            route=Screens.Chat.route,
            arguments = listOf(
                navArgument(
                    name = CHAT_ARGUMENT_JSON,
                ){
                  type = NavType.StringType
                },
            ),
            deepLinks = listOf(
                navDeepLink{
                    uriPattern= "$CHAT_URI_DEEPLINK/{${CHAT_ARGUMENT_JSON}}"
                }
            )
        ){
            //переделать, чтобы засунуть данные сразу в конструктор ViewModel

            val viewModel = hiltViewModel<ConversationViewModel>()
            ChatScreen(
                navController = navController,
                viewModel = viewModel
            )

        }
        composable(
            route = Screens.CreateGroupFirst.route,
            arguments = listOf(
                navArgument(
                    name =CREATEGROUP_ARGUMENT_USERID ,
                ){
                    type = NavType.StringType
                },
            )
        ){
            NewGroupSelectUsersScreen(navController)
        }
    }
}