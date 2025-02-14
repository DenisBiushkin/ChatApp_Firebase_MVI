package org.example.Mappers.base

interface Mapper<From,To> {

    fun map(from:From):To
}