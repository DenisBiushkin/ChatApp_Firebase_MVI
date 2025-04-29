package com.example.unmei.presentation.create_group_feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.R
import com.example.unmei.presentation.create_group_feature.utils.CircleSelectItemBox
import com.example.unmei.presentation.util.ui.theme.focusedTextFieldText
import com.example.unmei.presentation.util.ui.theme.textFieldContainer
import com.example.unmei.presentation.util.ui.theme.unfocusedTextFieldText


@Preview(showBackground = true)
@Composable
fun showCompanionTextFieldGroup(

){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        CompanionTextFieldGroup(
            onSelectClick = {

            },
            textChatName = "",
            onValueChange = {}
        )
    }
}

@Composable
fun CompanionTextFieldGroup(
    modifier: Modifier = Modifier,
    textChatName:String,
    onValueChange:(String)->Unit,
    painter: Painter?=null,
    onSelectClick:()->Unit
){
    val textFieldShape = RoundedCornerShape(10.dp)
    val width = LocalConfiguration.current.screenWidthDp.dp*0.6f
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(100.dp)
            .fillMaxWidth()
        , verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        if (painter!=null){
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                ,
                painter =painter,
                contentDescription = ""
            )
        }else{
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = Color.Black.copy(alpha = .2f),
                        shape = CircleShape
                    )
                    .clickable {
                        onSelectClick()
                    }
                ,
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription =""
                )
            }
        }

        Column(

        ) {
            TextField(
                colors = TextFieldDefaults.colors(
                    //фон подсказывающего текста
                    unfocusedPlaceholderColor  = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFieldText,
                    //фон контейнера
                    unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,//не выбран(не в фокусе)
                    focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer//выбран(в фокусе)
                ),shape = textFieldShape,
                modifier = Modifier
                    .height(50.dp)
                    .width(width)
                    .background(
                        color = Color.Black.copy(alpha = .3f),
                        shape = textFieldShape
                    )
                    .border(
                        shape = textFieldShape,
                        width = 1.dp,
                        color = Color.Blue.copy(alpha = .5f)
                    )
                ,
                value =textChatName ,
                onValueChange =onValueChange,
                placeholder = {
                    Text(
                        text = "Название",
                        fontSize = 14.sp,
                        color = LocalContentColor.current.copy(alpha = 0.6f)
                    )
                },
                maxLines = 1,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                .width(width),
                text = "Введите название и при желании загрузите фотографию",
                fontSize = 14.sp
            )
        }
    }

}