package com.example.unmei.util

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.unmei.domain.model.User
import com.example.unmei.util.PreferencesKeys.CURRENT_USER_SHARED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class ChatSessionManager(
    @ApplicationContext private val context: Context
) {

    private  val userSharedPref = context.getSharedPreferences(CURRENT_USER_SHARED, Context.MODE_PRIVATE)

    fun setCurrentUser(user: User){
       with(userSharedPref.edit()){
           putString("user",user.toJson())
           apply()
       }
    }
   fun getCurrentUser():User?{
       val data=  userSharedPref.getString("user", "empty")
       if (data=="empty")
           return null
       return data?.let { User.fromJson(it) }
   }
}




