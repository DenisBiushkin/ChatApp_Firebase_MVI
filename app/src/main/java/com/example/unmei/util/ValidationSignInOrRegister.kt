package com.example.unmei.util

import android.util.Patterns

object ValidationSignInOrRegister {

    fun validateRegister(
        fullName: String,
        email: String,
        password1: String,
        password2: String
    ): Resource<Unit>{
        if (fullName.isEmpty()){
            return Resource.Error(message = "Все поля должны быть заполнены")
        }
        if(password1!=password2){
            return Resource.Error(message = "Пароли должны совпадать")
        }
        val password = password1
        return validateSignIn(email, password)
    }

    fun validateSignIn(
        email: String,
        password: String
    ): Resource<Unit>{
        if (email.isEmpty() || password.isEmpty()){
            return Resource.Error(message = "Все поля должны быть заполнены")
        }
        if (password.length< 8){
            return  Resource.Error(message ="Пароль должен быть длинее 8 символов")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return  Resource.Error(message ="Некорректный Email")
        }
        return Resource.Success(Unit)
    }
}