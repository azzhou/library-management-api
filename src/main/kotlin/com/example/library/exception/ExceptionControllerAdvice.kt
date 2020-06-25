package com.example.library.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(BasicException::class)
    fun handleCustomException(ex: BasicException, request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val details = ErrorDetails(
            status = ex.status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = ex.message,
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, ex.status)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException,
                                     request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.BAD_REQUEST
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = "The body of the request is not readable. Please ensure the format meets requirements.",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(ex: AuthenticationException, request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.UNAUTHORIZED
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = "Email and password do not match any accounts on record",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException,
                           request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.FORBIDDEN
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = "Your account does not have access",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException,
                                            request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.METHOD_NOT_ALLOWED
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = "${ex.method} method is not supported. Supported methods are ${ex.supportedHttpMethods.toString()}",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(NumberFormatException::class)
    fun handleNumberFormat(ex: NumberFormatException,
                           request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.BAD_REQUEST
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf(ex::class.java.simpleName),
            message = "The input string cannot be converted to a number",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                     request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val result = ex.bindingResult
        val errorList = mutableListOf<String>()
        for (error in result.fieldErrors) {
            errorList.add(error.field + ": " + error.defaultMessage)
        }
        val status = HttpStatus.BAD_REQUEST
        val details = ErrorDetails(
            status = status.value(),
            errors = errorList,
            message = "Validation failed for argument(s) listed",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }

    @ExceptionHandler(Exception::class)
    fun handleOther(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorDetails> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val details = ErrorDetails(
            status = status.value(),
            errors = mutableListOf("InternalServerError"),
            message = "Something went wrong internally. Sorry!",
            path = request.requestURI,
            method = request.method
        )
        return ResponseEntity(details, status)
    }
}
