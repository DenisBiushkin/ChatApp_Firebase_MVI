package com.example.unmei.presentation.chat_list_feature.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.unmei.R

@Composable
@Preview(showBackground = true)
fun showEmptyScreen(){
    EmptyChatScreen()
}
@Composable
fun EmptyChatScreen(
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp * 0.8f

    // Анимация появления (fade-in)
    val alphaAnim by remember { mutableStateOf(1f) } // можно заменить на animateFloatAsState(...) если нужно

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .alpha(alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Эмодзи вместо картинки
            Text(
                text = "🫥",
                fontSize = 60.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Похоже, что у вас пока ещё нет активных чатов",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.widthIn(max = screenWidth)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Для создания группового чата нажмите (➕) вверху панели",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.widthIn(max = screenWidth)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Приватный можно создать через вкладку Друзья, начав чат с конкретным пользователем (💬)",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.widthIn(max = screenWidth)
            )
        }
    }
}