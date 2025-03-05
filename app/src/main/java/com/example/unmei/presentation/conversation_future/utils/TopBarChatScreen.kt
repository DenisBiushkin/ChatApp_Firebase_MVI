package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarChatScreen(
    modifier: Modifier = Modifier,
    onClickBack:()->Unit,
    onClickProfile: () -> Unit,
    statusChat:String,
    titleChat:String,
    iconChatPainter: Painter
){
    val colorApp= Color(0xFF42A5F5)
    val colorText= Color.White
    val colorTextLight = Color(0xFF90CAF9)

    val  expanded = remember { mutableStateOf(false) }
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
        , title = {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { onClickProfile() }
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                    , painter =iconChatPainter,
                    contentDescription =""
                )
                Column (
                    modifier = Modifier.padding(start = 10.dp)
                ){
                    //отредачить расстояние между текстами
                    //см проект ComposeTraining
                    Text(
                        text = titleChat,
                        color=colorText,
                        maxLines = 1,
                        fontSize = 20.sp
                    )
                    //Жестко отредачить и добавить анимацию когда кто то печатает
                    Text(
                        modifier = Modifier.padding(top = 0.dp),

                        text = statusChat,
                        color= colorTextLight,
                        maxLines = 1,
                        fontSize = 15.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 5.dp)
                    ,
                onClick = { onClickBack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        //DropDown меню
        actions = {
            IconButton(  modifier = Modifier
                .padding(end = 2.dp),
                onClick = { expanded.value = true }
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Показать меню"
                )
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                Text("Скопировать", fontSize=18.sp, modifier = Modifier.padding(10.dp))
                Text("Вставить", fontSize=18.sp, modifier = Modifier.padding(10.dp))
                HorizontalDivider()
                Text("Настройки", fontSize=18.sp, modifier = Modifier.padding(10.dp))
            }
        },
        colors= TopAppBarDefaults.topAppBarColors(
            containerColor = colorApp
        )
    )
}