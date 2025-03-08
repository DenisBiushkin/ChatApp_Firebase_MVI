package com.example.unmei.presentation.conversation_future.utils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import  androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp


@Composable
fun BottomButtonSelectMedia(
    mediaSelected:Boolean = false,
    countSelectedMedia:Int,
    onClick: () -> Unit,
    height: Dp = 60.dp
){
    Row (
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(Color.Blue),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(height-10.dp)
            , onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(mediaSelected) Color.White else Color.Gray,
              //  contentColor = if(mediaSelected)  Color.White else Color.Black
            )
            , shape = RoundedCornerShape(15.dp)
        ) {
            if(mediaSelected){
                Text(text = "Добавить")
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "$countSelectedMedia",color= Color.White)
                }
            }else{
                Text(text = "Отменить")
            }
        }
    }
}