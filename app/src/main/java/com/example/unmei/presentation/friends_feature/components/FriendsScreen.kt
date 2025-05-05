package com.example.unmei.presentation.friends_feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unmei.presentation.Navigation.Screens
import com.example.unmei.presentation.friends_feature.model.FriendItemUi
import com.example.unmei.presentation.friends_feature.model.FriendVMEvent
import com.example.unmei.presentation.friends_feature.model.FriendsContentState
import com.example.unmei.presentation.friends_feature.viewmodel.FriendsViewModel
import com.example.unmei.presentation.util.LoadingScreen


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
    FriendScreenTopAndSearchBar(
        onClickBack={
            navController.popBackStack()
        },
        onFocusedChanged = {viewModel.onEvent(FriendVMEvent.SearchActiveChanged(it.isFocused))},
        onTextFieldChanged = {  viewModel.onEvent(FriendVMEvent.SearchFieldChanged(it))},
        textField =state.value.searchQuery,
        searchActive = state.value.isSearchActive,
        onExiteSearch = {
            viewModel.onEvent(FriendVMEvent.SearchActiveChanged(false))
        }
    ){
        when(state.value.contentState){
            is FriendsContentState.Error -> {}
            is FriendsContentState.Loading -> { LoadingScreen(Modifier.background(Color.White))}
            is FriendsContentState.Content -> {
                FriendScreenContent(
                    isSearchActive = state.value.isSearchActive,
                    onClickAddUser = {
                        viewModel.onEvent(
                            FriendVMEvent.AddNewFriend(it.uid)
                        )
                    },
                    onClickItem = {
                        navController.navigate(
                            Screens.Profile.withJsonData(it.uid)
                        )
                    },
                    onClickSandMessage = {
                        navController.navigate(
                            Screens.Chat.withExistenceData(
                                chatExist = false,
                                chatUrl = it.iconUrl,
                                chatName = it.fullName,
                                companionUid = it.uid
                            )
                        )
                    },
                    othersList = state.value.searchResultList,
                    friendsList = state.value.myFriendsList
                )
            }
        }
    }
}

@Composable
fun FriendScreenContent(
    modifier: Modifier = Modifier,
    isSearchActive:Boolean,
    onClickItem:(FriendItemUi)->Unit,
    onClickSandMessage:(FriendItemUi)->Unit,
    onClickAddUser:(FriendItemUi)->Unit,
    friendsList:List<FriendItemUi>,
    othersList:List<FriendItemUi>
){
    LazyColumn(
        modifier = modifier
            .padding(top = 5.dp, start = (4).dp, end = (4).dp)
            .fillMaxSize()
            .imePadding()
    ) {
        if (isSearchActive){
            items( othersList){
                FriendListItemDataClass(
                    itemUi = it,
                    onClickAddUser = onClickAddUser,
                    onClickItem = onClickItem,
                    onClickSandMessage = onClickSandMessage
                )
            }
        }else{
            //блок сортировки @Composable()
            item(){
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Мои контакты ${friendsList.size}",
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(friendsList){

                FriendListItemDataClass(
                    itemUi = it,
                    onClickAddUser = onClickAddUser,
                    onClickItem = onClickItem,
                    onClickSandMessage = onClickSandMessage
                )
            }
        }

    }
}