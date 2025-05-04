package com.example.unmei.presentation.chat_list_feature.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.unmei.presentation.chat_list_feature.util.ShimmerChatItem

@Preview(showBackground = true)
@Composable
fun showAnimatedChatEffect(){
    AnimatedShimmerEffectChatItem()
}


@Composable
fun AnimatedShimmerEffectChatItem(){
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnimate = transition.animateFloat(
        initialValue = 0f,
        targetValue =1000f ,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Время выполнения одной анимации
                delayMillis = 0,
                easing = FastOutSlowInEasing// Интерполятор (ускорение-замедление)
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )
    val brush = Brush.linearGradient(
        colors= shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimate.value, y = translateAnimate.value)
    )
    ShimmerChatItem(brush = brush)
}

@Composable
fun ShimmerChatList(
    brush: Brush
){

}