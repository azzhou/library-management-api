package com.example.library.exception

import org.springframework.http.HttpStatus

class InvalidFinePaymentException : BasicException(
    message = "The payment amount must be between $0.00 and the total fine amount",
    status = HttpStatus.BAD_REQUEST
)