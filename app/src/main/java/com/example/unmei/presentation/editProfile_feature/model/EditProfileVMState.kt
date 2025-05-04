package com.example.unmei.presentation.editProfile_feature.model

import android.net.Uri

data class EditProfileVMState(
    val userId:String = "",
    val firstName:String="",
    val secondName:String="",
    val userName:String = "",
    val iconUrl:String = "",
    val contentState: EditProfileScreenContent = EditProfileScreenContent.LOADING,

    val showAlert: Boolean = false,
    val alertUpdatesResult: List<SaveResult> = emptyList(),
    val selectedUri:Uri  = Uri.EMPTY

)

