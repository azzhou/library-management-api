package com.example.library.exception

import java.time.LocalDateTime

data class ErrorDetails(
    val status: Int,
    val errors: MutableList<String>,
    val message: String?,
    val path: String,
    val method: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)