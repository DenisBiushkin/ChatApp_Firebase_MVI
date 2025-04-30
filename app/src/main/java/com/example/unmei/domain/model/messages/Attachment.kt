package com.example.unmei.domain.model.messages

import kotlinx.serialization.Serializable

sealed class Animal {
    abstract val name: String
    data class Dog(override val name: String, val breed: String) : Animal()
    data class Cat(override val name: String, val lives: Int) : Animal()
}

@Serializable
sealed class Attachment{
    abstract val base64data: String

    @Serializable
    class Image(
        override val base64data: String,
    ): Attachment()

    @Serializable
    class File(
        override val base64data: String,
        val filename:String
    ): Attachment()

    @Serializable
  class Audio(
      override val base64data: String,
        val duration: Float
    ): Attachment()


}