package com.example.unmei.presentation.friends_feature.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    viewModel:FriendsViewModel
){

        FriendScreenFull(
            onClickBack = {
                navController.popBackStack()
            },
            onClickSandMessage = {}
        )

}