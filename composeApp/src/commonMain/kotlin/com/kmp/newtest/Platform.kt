package com.kmp.newtest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform