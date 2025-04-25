package com.example.unmei.presentation.friends_feature.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.friends_feature.model.FriendVMEvent
import com.example.unmei.presentation.friends_feature.viewmodel.FriendsViewModel


@Preview(showBackground = true)
@Composable
fun show15(){
    FriendsScreen(
        navController = rememberNavController(),
       viewModel = hiltViewModel()
        )
}

@Composable
fun FriendsScreen(
    navController: NavController,
    viewModel:FriendsViewModel = hiltViewModel()
){

    val state = viewModel.state.collectAsState()
        FriendScreenFull(
            onClickBack = {
                navController.popBackStack()
            },
            onClickSandMessage = {
                navController.navigate(Screens.Chat.withExistenceData(
                    chatExist = false,
                    chatUrl = it.iconUrl,
                    chatName = it.fullName,
                    companionUid = it.uid
                ))
            },
            onClickAddUser = {

            },
            listFriends = state.value.myFriends,
            textField = state.value.searchQuery,
            onFocusedChanged = {
                it.isFocused
            },
            onTextFieldChanged = {
                viewModel.onEvent(FriendVMEvent.SearchFieldChanged(it))
            }
        )

}