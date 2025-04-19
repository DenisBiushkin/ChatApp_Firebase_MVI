package com.example.unmei.android_frameworks.notification

import java.util.zip.CRC32

object NotificationIdGenerator {

    fun fromString(key: String): Int {
        val crc = CRC32()
        crc.update(key.toByteArray())
        return (crc.value.toInt() and 0x7FFFFFFF) // чтобы был всегда положительный Int
    }
}