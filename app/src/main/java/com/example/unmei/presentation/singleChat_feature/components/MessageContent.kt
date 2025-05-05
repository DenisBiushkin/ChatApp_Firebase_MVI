package com.example.unmei.presentation.singleChat_feature.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.util.ui.theme.primaryOwnMessageColor
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
fun showMessageContent(

){
    Column(
        Modifier.fillMaxSize(),
    ) {
        val message =MessageListItemUI(
            timeString = "12:00",
            fullName = "Marcile Donato",
            text = "Привет",
            timestamp = LocalDateTime.now(),
        )
        ChatBubbleWithPattern(
            modifier= Modifier,
            isOwn = message.isOwn,
        ) {
            MessageContent(
                data = message
            )
        }
        ChatBubbleWithPattern(
            modifier= Modifier,
            isOwn = true,
        ) {
            MessageContent(
                data = message.copy(isOwn = true)
            )
        }
    }
}


@Composable
fun MessageContent(
    data: MessageListItemUI,
    showFullName: Boolean = true
) {
    val textFontSize = 15.sp
    val metaFontSize = 12.sp

    val padding =8.dp
    Column(
        modifier = Modifier
            .padding(
                top =  padding,
                bottom = 0.dp,
                start =  padding,
                end =  padding,
//                horizontal = 8.dp, vertical = 6.dp
            ), // базовые внутренние отступы
        verticalArrangement = Arrangement.spacedBy(2.dp), // равномерное расстояние между элементами
        horizontalAlignment = Alignment.Start
    ) {
        if (showFullName) {
            Text(
                text = data.fullName,
                fontSize = metaFontSize,
                color = if (!data.isOwn) primaryOwnMessageColor else LocalContentColor.current,
                fontWeight = if (!data.isOwn) FontWeight.Bold else FontWeight.Normal,
                lineHeight = 14.sp
            )
        }

        Text(
            text = data.text,
            fontSize = textFontSize,
            lineHeight = 18.sp
        )

        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (data.isChanged) {
                Text(text = "ред.", fontSize = metaFontSize)
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(text = data.timeString, fontSize = metaFontSize)

            if (data.isOwn) {
                Spacer(modifier = Modifier.width(4.dp))
                MessageIconStatus(
                    status = data.status,
                    sizeIcon = 14.dp
                )
            }
        }
    }
}
@Composable
fun MessageContentForSingleChat(
    data: MessageListItemUI,
    showFullName:Boolean = false
) {
    val textFontSize = 15.sp
    val metaFontSize = 12.sp
    val padding = 10.dp

    Box(
        modifier = Modifier
            .padding(
                top = padding,
                bottom = 0.dp,
                start = padding,
                end = padding,
            )
            .wrapContentSize()
    ) {
        Column (
            modifier = Modifier.padding(end = 50.dp, bottom = 18.dp)
        ){
            if (showFullName) {
                Text(
                    text = data.fullName,
                    fontSize = metaFontSize,
                    color = if (!data.isOwn) primaryOwnMessageColor else LocalContentColor.current,
                    fontWeight = if (!data.isOwn) FontWeight.Bold else FontWeight.Normal,
                    lineHeight = 14.sp
                )
            }
            Text(
                text = data.text,
                fontSize = textFontSize,
                lineHeight = 18.sp,
                color = Color.Black,
                // оставить место под мета-инфу
            )
        }

Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (data.isChanged) {
                Text(text = "ред.", fontSize = metaFontSize, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(text = data.timeString, fontSize = metaFontSize, color = Color.Gray)

            if (data.isOwn) {
                Spacer(modifier = Modifier.width(4.dp))
                MessageIconStatus(
                    status = data.status,
                    sizeIcon = 14.dp
                )
            }
        }
    }
}