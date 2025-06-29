package com.example.unmei.presentation.Navigation

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.unmei.presentation.singleChat_feature.components.ChatScreen
import com.example.unmei.presentation.chat_list_feature.components.DrawerScreen

import com.example.unmei.presentation.singleChat_feature.viewmodel.ConversationViewModel
import com.example.unmei.presentation.create_group_feature.components.NewGroupSelectUsersScreen
import com.example.unmei.presentation.editProfile_feature.components.EditProfileScreenFull
import com.example.unmei.presentation.friends_feature.components.FriendsScreen
import com.example.unmei.presentation.friends_feature.viewmodel.FriendsViewModel
import com.example.unmei.presentation.groupChat_feature.components.GroupChatScreen
import com.example.unmei.presentation.profile_user_feature.components.ProfileUserFull
import com.example.unmei.presentation.profile_user_feature.viewmodel.ProfileUserViewModel
import com.example.unmei.presentation.sign_in_feature.sign_in.GoogleAuthUiClient
import com.example.unmei.util.ConstansApp
import com.example.unmei.util.ConstansApp.CHAT_ARGUMENT_JSON
import com.example.unmei.util.ConstansApp.CHAT_URI_DEEPLINK
import com.example.unmei.util.ConstansApp.CREATEGROUP_ARGUMENT_USERID
import com.example.unmei.util.ConstansApp.EDITPROFILE_ARGUMENT_USERID
import com.example.unmei.util.ConstansApp.GROUPCHAT_ARGUMENT_CHATID
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
            val viewModel = hiltViewModel<ProfileUserViewModel>()
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


        composable(
            route = Screens.GroupChat.route,
            arguments = listOf(
                navArgument(
                    name =GROUPCHAT_ARGUMENT_CHATID ,
                ){
                    type = NavType.StringType
                },
            )
        ){
            GroupChatScreen(navController)
        }

        composable(
            route = Screens.EditProfile.route,
            arguments = listOf(
                navArgument(
                    name =EDITPROFILE_ARGUMENT_USERID,
                ){
                    type = NavType.StringType
                },
            )
        ){
            EditProfileScreenFull(navController)
        }
    }
}