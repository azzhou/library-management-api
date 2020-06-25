package com.example.library.exception

import org.springframework.http.HttpStatus

class UserNotFoundException(userId: Long) : BasicException("Could not find user $userId", HttpStatus.NOT_FOUND)

class UserAlreadyExistsException(email: String) : BasicException("$email is already in use", HttpStatus.CONFLICT)