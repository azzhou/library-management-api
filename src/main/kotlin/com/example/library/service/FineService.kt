package com.example.library.service

import com.example.library.entity.User
import org.springframework.security.access.prepost.PreAuthorize

// Not doing much now, but potentially could incorporate Stripe, Square, Paypal, etc.
interface FineService {

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #userId == authentication.principal.id")
    fun payFine(userId: Long, paymentAmount: Long): User

    fun updateFines()
}