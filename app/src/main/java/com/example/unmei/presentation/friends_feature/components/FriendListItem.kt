package com.example.unmei.presentation.friends_feature.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.R
import com.example.unmei.presentation.chat_list_feature.components.BoxWithImageUser

@Preview(showBackground = true)
@Composable
fun showFriendListItem(){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        items(5){
            FriendListItem(
                isOnline =it % 2==0,
                painterIcon = painterResource(id = R.drawable.test_user),
                fullName = "Marcile Donato",
                onClickSendMessage = {},
                onClickAddUser = {},
                onClickItem = {},
                isFriend = it % 2==0
            )
        }
    }

//    Box(
//        modifier = Modifier.fillMaxSize()
//    ){
//        BoxWithImageUser(
//            modifier = Modifier,
//            painterUser = painterResource(id = R.drawable.test_user),
//            height = 50.dp,
//            statusCircleRadius = 5.dp,
//            isOnline = true
//        )
//    }
}


@Composable
fun FriendListItem(
    modifier: Modifier = Modifier,
    painterIcon: Painter,
    fullName: String,
    isOnline: Boolean,
    isFriend:Boolean,
    onClickSendMessage: () -> Unit,
    onClickAddUser: ()->Unit,
    onClickItem:()->Unit,

) {
    val rowHeight = 60.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clickable { onClickItem() }
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FriendImagePart(
            painterIcon = painterIcon,
            isOnline = isOnline,
            modifier = Modifier.weight(0.13f),
            rowHeight = rowHeight
        )

        FriendDescriptionPart(
            fullName = fullName,
            modifier = Modifier.weight(0.7f),
            rowHeight = rowHeight
        )

        FriendActionsPart(
            imageVector =  ImageVector.vectorResource(id = if(isFriend) R.drawable.chat_bubble_24px else R.drawable.person_add_24px),
            onClickSendMessage = if (isFriend) onClickSendMessage else onClickAddUser,
            modifier = Modifier.weight(0.17f),
            rowHeight = rowHeight
        )
    }
}
@Composable
fun FriendImagePart(
    modifier: Modifier = Modifier,
    painterIcon: Painter,
    isOnline: Boolean = false,
    rowHeight: Dp = 40.dp
) {
    Box(
//        modifier = modifier
//            .height(rowHeight),
        contentAlignment = Alignment.Center
    ) {
        BoxWithImageUser(
            modifier = Modifier.padding(start = 5.dp),
            painterUser = painterIcon,
            height = rowHeight,
            statusCircleRadius = 5.dp,
            isOnline = isOnline
        )
    }
}

@Composable
fun FriendDescriptionPart(
    fullName: String,
    modifier: Modifier = Modifier,
    rowHeight: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .height(rowHeight),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(start = 7.dp),
            text = fullName,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            fontSize = 15.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FriendActionsPart(
    imageVector: ImageVector,
    onClickSendMessage: () -> Unit,
    modifier: Modifier = Modifier,
    rowHeight: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .height(rowHeight),
        contentAlignment = Alignment.Center
    ) {


        IconButton(
            modifier = Modifier.clip(CircleShape),
            onClick = onClickSendMessage
        ) {
            Icon(
                imageVector =imageVector,
                contentDescription = "",
                tint = Color.Blue
            )
        }

    }
}
