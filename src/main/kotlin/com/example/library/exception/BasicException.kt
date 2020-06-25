package com.example.library.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

// Base for custom exceptions that is handled in Controller Advice
// All exceptions in Kotlin are unchecked, so no difference between extending RuntimeException and Exception
// But in Java, we'd use RuntimeException (unchecked) because we rely on ExceptionControllerAdvice class to handle
// instead of using try/catch blocks
open class BasicException(message: String, val status: HttpStatus) : RuntimeException(message)