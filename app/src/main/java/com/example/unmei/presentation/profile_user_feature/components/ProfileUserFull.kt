package com.example.unmei.presentation.profile_user_feature.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.profile_user_feature.model.ProfileDetailScreenContent
import com.example.unmei.presentation.profile_user_feature.viewmodel.ProfileUserViewModel
import com.example.unmei.presentation.util.LoadingScreen
import com.example.unmei.presentation.util.ui.theme.colorApp
import com.example.unmei.util.ConstansDev.TAG





@Composable
fun ProfileUserFull(
    navController: NavController,
    viewmodel: ProfileUserViewModel
) {
   val state = viewmodel.state.collectAsState()

    when(state.value.content){
        ProfileDetailScreenContent.LOADING -> LoadingScreen()
        ProfileDetailScreenContent.ERROR -> {}
        ProfileDetailScreenContent.CONTENT -> {
            ProfileUserScreen(
                backOnClick = {
                    navController.popBackStack()
                },
                fullName = state.value.fullName,
                painterIcon = rememberAsyncImagePainter(model = state.value.iconUrl),
                statusOnline =state.value.statusUser,
                isMine = state.value.isMine,
                listInfoUser = state.value.listInfo,
                messageOnClick={
                    //TO DO !state.value.userId.isEmpty()
                    if (!state.value.isMine){
                        Log.d(TAG,"Попали в условие для перехода")
                        navController.navigate(
                            Screens.Chat.withExistenceData(
                                chatExist = false,
                                companionUid = state.value.userId,
                                chatName = state.value.fullName,
                                chatUrl = state.value.iconUrl
                            ))
                    }
                },
                editOnClick = {
                    navController.navigate(
                        Screens.EditProfile.withUserId(state.value.userId)
                    )
                }
            )
        }
    }


}

