package com.example.unmei.presentation.friends_feature.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unmei.R
import com.example.unmei.domain.model.UserExtended
import com.example.unmei.presentation.friends_feature.model.FriendItemUi
import com.example.unmei.presentation.friends_feature.model.FriendVMEvent


@Preview(showBackground = true)
@Composable
fun showfriendScreenFull(

){
    FriendScreenFull(
        listFriends = listOf(),
        textField = "",
        onClickBack = {},
        onClickSandMessage = {},
        onClickAddUser = {},
        onFocusedChanged = {

        },
        onTextFieldChanged = {

        }
    )


}
@Composable
fun FriendScreenFull(
    listFriends:List<FriendItemUi>,
    textField:String,
    onClickBack: () -> Unit,
    onClickSandMessage:(FriendItemUi)->Unit,
    onClickAddUser:(FriendItemUi)->Unit,
    onFocusedChanged:(FocusState)->Unit,
    onTextFieldChanged: (String)->Unit
){

    val query = remember {
        mutableStateOf("")
    }

}
@Composable
fun  FriendListItemDataClass(
    itemUi:FriendItemUi,
    onClickItem:(FriendItemUi)->Unit,
    onClickSandMessage:(FriendItemUi)->Unit,
    onClickAddUser:(FriendItemUi)->Unit,
){
    FriendListItem(
        isOnline =itemUi.isOnline,
        painterIcon = rememberAsyncImagePainter(model = itemUi.iconUrl),
        fullName = itemUi.fullName,
        onClickSendMessage ={
            onClickSandMessage(itemUi)
        },
        onClickAddUser = {
            onClickAddUser(itemUi)
        },
        isFriend = itemUi.isFriend,
        onClickItem = {
            onClickItem(itemUi)
        }
    )
}
@Composable
fun FriendScreenTopAndSearchBar(
    onClickBack:()->Unit,
    searchActive:Boolean,
    onExiteSearch:()->Unit,
    textField:String,
    onTextFieldChanged:(String)->Unit,
    onFocusedChanged:(FocusState)->Unit,
    content: @Composable() (ColumnScope.()->Unit)
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(
                visible = !searchActive,
            ) {
                FriendTopBar(onClickBack = onClickBack)
            }
        }
    ) {
        paddingValues ->
        val horizontalPadding= 8.dp
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300, // скорость в миллисекундах
                        easing = FastOutSlowInEasing
                    )
                )
        ) {
            SearchBar(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding
                ),
                query = textField,
                onQueryChange = onTextFieldChanged,
                onFocusedChanged = onFocusedChanged,
                onExitSearch = onExiteSearch,
                isActiveSearch = searchActive
            )
            content()
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