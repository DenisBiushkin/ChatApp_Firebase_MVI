package com.example.unmei.presentation.editProfile_feature.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.unmei.presentation.editProfile_feature.model.AlertStatus
import com.example.unmei.presentation.editProfile_feature.model.EditProfileScreenContent
import com.example.unmei.presentation.editProfile_feature.viewmodel.EditProfileViewModel
import com.example.unmei.presentation.util.LoadingScreen
import com.example.unmei.util.ConstansDev.TAG

@Composable
fun EditProfileScreenFull(
    navController: NavController,
    viewmodel: EditProfileViewModel = hiltViewModel()
){
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // разрешение выдано, можно продолжить
        } else {
            // отказано
        }
    }
    val iconLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // uri может быть null, если пользователь ничего не выбрал
        if (uri != null) {
            Log.d(TAG, "Выбрано изображение: $uri")
            viewmodel.onSelectIconUri(uri)
        } else {
            Log.d(TAG, "Изображение не выбрано")
        }
    }
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
                    viewmodel.changeFields(firstName, lastName, username)
                },
                profilePhotoUrl = state.value.iconUrl,
                onBackClick = {
                    navController.popBackStack()
                },
                saveProfile = {
                    viewmodel.onAcceptData()
                },
                onChangePhoto = {
                    iconLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
            if(state.value.showAlert){
                AlertScreen(
                   alertUpdatesResult = state.value.alertUpdatesResult
                )
            }
        }

    }

}