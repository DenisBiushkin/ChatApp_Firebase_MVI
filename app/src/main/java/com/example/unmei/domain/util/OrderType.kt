package com.example.unmei.domain.util

sealed class OrderType {//тип сортировки
    object Ascending:OrderType()//по возрастанию
    object Descending:OrderType()//по убыванию
}