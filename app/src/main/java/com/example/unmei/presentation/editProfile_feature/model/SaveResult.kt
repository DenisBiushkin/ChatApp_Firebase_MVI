package com.example.unmei.presentation.editProfile_feature.model

sealed class SaveResult (
    val field:Field
){
     class Success(field:Field  ) : SaveResult(field)
     class Error(field: Field , val message: String) : SaveResult(field)
     class Loading(field: Field ): SaveResult(field)
}