package com.example.library.security

import java.time.LocalDateTime

data class AuthenticationDetails(
    val token: String,
    val expiresAt: LocalDateTime
)
