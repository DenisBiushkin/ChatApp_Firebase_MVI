package com.example.unmei.util

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PreferencesKeys {
    val CURRENT_USER_ID = stringPreferencesKey("current_user_id")

    val CURRENT_CHAT_ID = stringPreferencesKey("current_chat_id")
    val MUTED_CHATS = stringSetPreferencesKey("muted_chats")

    val CURRENT_USER_SHARED="user_shared"
}