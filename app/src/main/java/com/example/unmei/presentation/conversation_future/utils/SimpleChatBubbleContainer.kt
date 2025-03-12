package com.example.unmei.presentation.conversation_future.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SimpleChatBubbleContainer(
    modifier: Modifier = Modifier,
    isOwn: Boolean=false,
    cornerRadius: Dp =  15.dp,
    content: @Composable (BoxScope.() -> Unit)
){
    val screenSettings= LocalConfiguration.current
    val maxWidth=screenSettings.screenWidthDp.dp *0.8f// 80% экрана
    val maxHeight=screenSettings.screenHeightDp.dp *0.6f// 70%
    val paddingBlock= 8.dp
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(isOwn) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = if(isOwn){
                    modifier
                        .padding(end =paddingBlock)
                   .clip(RoundedCornerShape(cornerRadius))
                    .widthIn(max = maxWidth)
                    .heightIn(max = maxHeight)
            }else{
                modifier
                    .padding(start = paddingBlock)
                    .clip(RoundedCornerShape(cornerRadius))
                    .widthIn(max = maxWidth)
                    .heightIn(max = maxHeight)
            },
            content = content,
            contentAlignment = Alignment.BottomEnd
        )

    }
}