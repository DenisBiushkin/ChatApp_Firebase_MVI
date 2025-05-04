package com.example.unmei.presentation.editProfile_feature.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unmei.presentation.editProfile_feature.model.AlertStatus
import com.example.unmei.presentation.editProfile_feature.model.SaveResult

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AlertScreen(
    alertUpdatesResult: List<SaveResult>
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = false) {}, // блокирует клики
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = alertUpdatesResult,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { updatesResultList ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    updatesResultList.forEach {
                            updateResult->
                        when(updateResult){
                            is SaveResult.Error -> {
                                Text("❌ ${updateResult.field.value} — ${updateResult.message}")
                            }
                            is SaveResult.Loading -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${updateResult.field.value} — загрузка")
                                }
                            }
                            is SaveResult.Success -> {
                                Text("✅ ${updateResult.field.value} — сохранено")
                            }
                        }
                    }
                }

            }
        }
    }
}