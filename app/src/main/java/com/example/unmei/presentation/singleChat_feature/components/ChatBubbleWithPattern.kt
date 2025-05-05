package com.example.unmei.presentation.singleChat_feature.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unmei.presentation.chat_list_feature.model.MessageStatus
import com.example.unmei.presentation.util.ui.theme.primaryMessageColor
import com.example.unmei.presentation.util.ui.theme.primaryOwnMessageColor
import com.example.unmei.presentation.chat_list_feature.util.MessageIconStatus
import com.example.unmei.presentation.singleChat_feature.model.MessageListItemUI
import com.example.unmei.presentation.singleChat_feature.model.MessageType
import com.example.unmei.presentation.singleChat_feature.utils.CustomBubbleMessageShape
import com.example.unmei.presentation.util.ui.theme.chatBacgroundColor
import com.example.unmei.presentation.util.ui.theme.colorApp
import java.time.LocalDateTime
@Composable
fun ChatBubbleWithPattern(
    modifier: Modifier = Modifier,
    isOwn:Boolean= false,
    content: @Composable (ColumnScope.() -> Unit)
){
    val otherPadding = 5.dp
    val compensatePadding=15.dp
    val screenSettings=LocalConfiguration.current
    val maxWidth=screenSettings.screenWidthDp.dp *0.8f
    val density = LocalDensity.current
    val cornerRadius = 15.dp
    val leftMessageShape = remember {
        CustomBubbleMessageShape(density=density, cornerRadiusDp =cornerRadius)
    }
    val rightMessageShape = remember {
        CustomBubbleMessageShape(density=density,position = true, cornerRadiusDp = cornerRadius)
    }

    val modifierLeftMassage= modifier
        .padding(
            start = compensatePadding,
            top = otherPadding,
            bottom = otherPadding,
            end = otherPadding
        )
        .widthIn(max = maxWidth)

    val modifierRightMassage = modifier
        .padding(
            start = otherPadding,
            top = otherPadding,
            bottom = otherPadding,
            end = compensatePadding
        )
        .widthIn(max = maxWidth)


    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
        if(isOwn) Arrangement.End else Arrangement.Start
    ) {

        if (isOwn) {
            Box(
                modifier = modifier
                    .clip(shape = rightMessageShape)
                    .background(primaryOwnMessageColor)
            ) {
                Column(
                    modifier = modifierRightMassage
                ) {
                    content()
                }
            }
        } else {
            Box(
                modifier = modifier
                    .clip(shape = leftMessageShape)
                    .background(color = primaryMessageColor)
            ) {
                Column(
                    modifier = modifierLeftMassage
                ) {
                    content()
                }
            }
        }
    }

}