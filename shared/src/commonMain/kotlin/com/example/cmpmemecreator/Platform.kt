package com.example.cmpmemecreator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform