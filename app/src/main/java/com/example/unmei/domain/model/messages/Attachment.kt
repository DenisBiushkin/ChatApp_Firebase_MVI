package com.example.unmei.domain.model.messages

import com.example.unmei.presentation.singleChat_feature.model.AttachmentTypeUI
import com.example.unmei.presentation.singleChat_feature.model.AttachmentUi
import kotlinx.serialization.Serializable

sealed class Animal {
    abstract val name: String
    data class Dog(override val name: String, val breed: String) : Animal()
    data class Cat(override val name: String, val lives: Int) : Animal()
}

@Serializable
sealed class Attachment{
    abstract val attachUrl: String

    @Serializable
    class Image(
        override val attachUrl: String,
    ): Attachment()

    @Serializable
    class File(
        override val attachUrl: String,
        val filename:String
    ): Attachment()

    @Serializable
  class Audio(
        override val attachUrl: String,
        val duration: Float
    ): Attachment()

}