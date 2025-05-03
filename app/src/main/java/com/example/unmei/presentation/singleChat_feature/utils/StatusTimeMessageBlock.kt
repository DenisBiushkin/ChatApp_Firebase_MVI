package com.example.unmei.presentation.singleChat_feature.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus

@Composable
fun StatusTimeMessageBlock(
    modifier: Modifier = Modifier,
    colorText: Color = Color.White,
    containerColor:Color = Color.Black.copy(alpha = 0.3f),
    isChanged:Boolean = false,
    status: MessageStatus = MessageStatus.None,
    stringTime:String = ""

){
    val fontSizeStatus = 12.sp
        Box(
           modifier = modifier
               .clip(CircleShape)
               .wrapContentSize()
               .background(color = containerColor)
               .padding(horizontal = 4.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isChanged)
                    //изменить на на Annoted string что не юзать 2 TExt
                    Text(
                        text = "ред.",
                        fontSize = fontSizeStatus,
                        color = colorText
                    )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text =stringTime,
                    fontSize = fontSizeStatus,
                    color = colorText
                )
                Spacer(modifier = Modifier.width(4.dp))
                //убрать сравнение тут и сравнивать в viemodel Status.None
                MessageIconStatus(
                    status=status,
                    sizeIcon = 18.dp,
                    colorIcon = colorText
                )
            }
        }
}