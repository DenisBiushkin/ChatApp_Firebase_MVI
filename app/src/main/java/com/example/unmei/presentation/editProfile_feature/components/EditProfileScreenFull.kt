package com.example.unmei.presentation.editProfile_feature.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unmei.presentation.editProfile_feature.model.EditProfileScreenContent
import com.example.unmei.presentation.editProfile_feature.viewmodel.EditProfileViewModel
import com.example.unmei.presentation.util.LoadingScreen

@Composable
fun EditProfileScreenFull(
    navController: NavController,
    viewmodel: EditProfileViewModel = hiltViewModel()
){

    val state = viewmodel.state.collectAsState()
    when(state.value.contentState){
        EditProfileScreenContent.LOADING -> LoadingScreen()
        EditProfileScreenContent.ERROR -> {}
        EditProfileScreenContent.CONTENT -> {
            EditProfileScreen(
                fullName = Pair(state.value.firstName,state.value.secondName),
                username = state.value.userName,
                onValueChange = {
                        firstName, lastName, username->
                },
                profilePhotoUrl = state.value.iconUrl,
                onBackClick = {
                    navController.popBackStack()
                },
                saveProfile = {

                }
            )
        }
    }

}