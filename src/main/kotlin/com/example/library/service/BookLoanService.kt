package com.example.library.service

import com.example.library.entity.Loan
import com.example.library.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize

interface BookLoanService {

    @Secured("ROLE_ADMIN", "ROLE_LIBRARIAN")
    fun getAllLoans(pageable: Pageable): Page<Loan>

    // Use PostAuthorize instead of PreAuthorize to be able to check the user id within the loan object
    @PostAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or " +
        "returnObject.user.id == authentication.principal.id")
    fun getLoanWithLoanId(loanId: Long): Loan

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #userId == authentication.principal.id")
    fun getLoansWithUserId(userId: Long, pageable: Pageable): Page<Loan>

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #userId == authentication.principal.id")
    fun borrowBook(userId: Long, barcode: Long): Loan

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #loan.user.id == authentication.principal.id")
    fun returnBook(loan: Loan)

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #loan.user.id == authentication.principal.id")
    fun renewBook(loan: Loan): Loan

    fun isUserEligibleToRenew(user: User): Boolean

    fun isUserEligibleToBorrow(user: User): Boolean
}