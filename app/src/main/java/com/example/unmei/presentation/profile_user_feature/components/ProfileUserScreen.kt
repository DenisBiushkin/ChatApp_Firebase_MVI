package com.example.unmei.presentation.profile_user_feature.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon

import com.example.unmei.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.profile_user_feature.model.BlockInfo


@Preview(showBackground = true)
@Composable
fun showProfileUser(){
    ProfileUserScreen(
        backOnClick = {},
        messageOnClick = {},
        fullName = "Marcile Donato",
        statusOnline = "Offline",
        painterIcon = painterResource(id = R.drawable.test_user),
        isMine = false,
        editOnClick = {

        },
        listInfoUser= emptyList()

    )
}
@Composable
fun ProfileUserScreen(
    messageOnClick:()->Unit,
    editOnClick:()->Unit,
    backOnClick:()->Unit,
    fullName:String,
    statusOnline:String,
    painterIcon: Painter,
    isMine:Boolean,
    listInfoUser:List<BlockInfo>
){


    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        TopPart(
            backOnClick= backOnClick,
            fullName = fullName,
            painterIcon = painterIcon,
            statusOnline = statusOnline
        )
        val height = LocalConfiguration.current.screenHeightDp.dp
        val widh= LocalConfiguration.current.screenWidthDp.dp
        val sizeFloating= 50.dp
        Box(modifier = Modifier.fillMaxSize()){
            BottomPart(
                listInfoUser=listInfoUser
            )
            if(!isMine){
                FloatingActionButton(
                    modifier = Modifier
                        .size(sizeFloating)
                        .offset(y = -25.dp, x = widh - sizeFloating - 30.dp),
                    onClick = messageOnClick,
                    backgroundColor = Color.White
                ) {
                    Icon(
                        modifier = Modifier
                            .size(sizeFloating-20.dp),
                        imageVector= ImageVector.vectorResource(id = R.drawable.chat_24px),
                        tint = Color.Black ,
                        contentDescription =""
                    )
                }
            }else{
                FloatingActionButton(
                    modifier = Modifier
                        .size(sizeFloating)
                        .offset(y = -25.dp, x = widh - sizeFloating - 30.dp),
                    onClick = editOnClick,
                    backgroundColor = Color.White
                ) {
                    Icon(
                        modifier = Modifier
                            .size(sizeFloating-20.dp),
                        imageVector= ImageVector.vectorResource(id = R.drawable.edit_24px),
                        tint = Color.Black ,
                        contentDescription =""
                    )
                }

            }


        }

    }
}