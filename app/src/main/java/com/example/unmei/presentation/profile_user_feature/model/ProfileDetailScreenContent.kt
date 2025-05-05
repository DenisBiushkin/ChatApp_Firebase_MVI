package com.example.unmei.presentation.profile_user_feature.model

enum class ProfileDetailScreenContent(
    val value: String? = null
){
    LOADING,
    CONTENT,
    ERROR("Ошибка получения данных о пользователе")
}