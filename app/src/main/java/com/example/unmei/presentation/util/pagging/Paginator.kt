package com.example.unmei.presentation.util.pagging

interface Paginator<Key,Item> {

    suspend fun loadNextItems()
    fun reset()
}