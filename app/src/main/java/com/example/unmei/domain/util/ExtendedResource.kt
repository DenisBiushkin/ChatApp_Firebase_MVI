package com.example.unmei.domain.util

import com.example.unmei.util.Resource

sealed class  ExtendedResource<T>(
    val data:T? = null,
    val messsage: String? = null
) {
    class Added<T>(data: T?):ExtendedResource<T>(data)

    class Removed<T>(data: T?):ExtendedResource<T>(data)

    class Edited<T>(data: T?):ExtendedResource<T>(data)

    class Error<T>(data: T?=null,message: String): ExtendedResource<T>(data,message)


}