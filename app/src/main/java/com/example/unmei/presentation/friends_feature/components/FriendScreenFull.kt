package com.example.unmei.presentation.friends_feature.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R
import com.example.unmei.presentation.friends_feature.model.FriendItemUi


@Preview(showBackground = true)
@Composable
fun showfriendScreenFull(

){
    FriendScreenFull(
        listFriends = listOf(),
        onClickBack = {},
        onClickSandMessage = {}
    )


}
@Composable
fun FriendScreenFull(
    listFriends:List<FriendItemUi>,
    onClickBack: () -> Unit,
    onClickSandMessage:()->Unit
){

    val query = remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FriendTopBar(
                onClickBack =onClickBack
            )
        }
    ) {
        paddingValues ->
        val horizontalPadding= 8.dp
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            SearchBar(
                modifier = Modifier.padding(
                    horizontal =  horizontalPadding
                ),
                query = query.value, onQueryChange ={
                query.value = it
            } )
            LazyColumn(
                modifier = Modifier
                    .padding(top = 5.dp,
                        start =  (horizontalPadding/2.dp).dp,
                        end =   (horizontalPadding/2.dp).dp
                    )
                    .fillMaxWidth()
            ) {
                items( listFriends){
                    FriendListItem(
                        isOnline =it.isOnline,
                        painterIcon = rememberAsyncImagePainter(model = it.iconUrl),
                        fullName = it.fullName,
                        onClickSendMessage =onClickSandMessage,
                        onClickAddUser = {},
                        isFriend = it.isFriend,
                        onClickItem = {}
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendTopBar(
    onClickBack:()->Unit,
){
    TopAppBar(
        title = { 
            Text(
                text = "Друзья",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        },
       navigationIcon = {
           IconButton(onClick =onClickBack) {
               Icon(
                   imageVector = Icons.Default.ArrowBack,
                   contentDescription = "",
                   tint = Color.Blue
               )
           }
       }
    )
}