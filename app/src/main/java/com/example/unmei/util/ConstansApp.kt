package com.example.unmei.util

object ConstansApp {

    val AUTH_NAVIGATE_ROUTE ="auth_route"
    val ROOT_NAVIGATE_ROUTE="root_route"
    val MAIN_NAVIGATE_ROUTE="main_route"
    val ONBOARDING_NAVIGATE_ROUTE="onboarding_route"

    val CHAT_AGUIMENT_GROUPUID = "groupUid"
    val CHAT_ARGUMENT_COMPANIONUID = "companionUid"
    val CHAT_ARGUMENT_JSON="json_existence_data"
    val PROFILE_ARGUMENT_JSON="json_profile_data"

    //deeplink
    val CHAT_URI_DEEPLINK="myapp://chat_screen"

    //Retrofit
    val FCM_SEND_NOTIFICATION_BASE_URL="https://fcm.googleapis.com/"
    val FCM_TOKEN_GET_URL="https://www.googleapis.com/auth/firebase.messaging"
    val FCM_SERVICE_ACCOUNT_FILENAME="service-account-fcm.json"

    //DB
    val USERS_REFERENCE_DB="users_unmei"
    val ROOMS_REFERENCE_DB="groups"
    val MESSAGES_REFERENCE_DB= "messages"
    val CHATS_BY_USERS_REFERENCE_DB="chats_by_users"
    val PRESENCE_USERS_REFERENCE_DB="presence_users"
    val MESAGES_SUMMERIES_DB = "message_summaries"
    val NOTIFICATION_TOKENS_DB="notification_tokens_users"

    //Storage
    val ROOMS_REFERENCE_STORAGE = "Rooms"

    //Android Fraimworks
    val BROCAST_MESSAGE_KEY="messageextra"
    val KEY_TEXT_REPLY = "key_text_reply"
    val NEW_MESSAGE_CHANNEL_ID="message_channel_id"
    val MESSAGE_CHANNEL_NAME="Уведомления сообщений"
}