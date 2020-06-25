package com.example.library.service

import com.example.library.entity.BookStatus
import com.example.library.entity.Loan
import com.example.library.entity.User
import com.example.library.exception.*
import com.example.library.repository.BookCopyRepository
import com.example.library.repository.LoanRepository
import com.example.library.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


const val LOAN_LENGTH_DAYS = 14
const val MAX_NUM_RENEWALS = 2
const val RENEWAL_FINE_LIMIT_IN_CENTS = 100L
const val BORROWING_FINE_LIMIT_IN_CENTS = 100L
const val MAX_NUM_BOOK_LOANS = 20


@Service
class BookLoanServiceImpl @Autowired constructor(
    private val bookCopyRepository: BookCopyRepository,
    private val loanRepository: LoanRepository,
    private val userRepository: UserRepository
) : BookLoanService {

    override fun getAllLoans(pageable: Pageable): Page<Loan> {
        return loanRepository.findAll(pageable)
    }

    override fun getLoanWithLoanId(loanId: Long): Loan {
        return loanRepository.findByIdOrNull(loanId) ?: throw LoanNotFoundException(loanId)
    }

    fun getLoanWithUserIdAndBarcode(userId: Long, barcode: Long): Loan {
        return loanRepository.findByUser_idAndBookCopy_barcode(userId, barcode)
            ?: throw LoanNotFoundException(userId, barcode)
    }

    override fun getLoansWithUserId(userId: Long, pageable: Pageable): Page<Loan> {
        userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        return loanRepository.findByUser_id(userId, pageable)
    }

    override fun borrowBook(userId: Long, barcode: Long): Loan {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
        val bookCopy = bookCopyRepository.findByIdOrNull(barcode) ?: throw BookCopyNotFoundException(barcode)

        // Check if user is allowed to borrow this copy
        if (!isUserEligibleToBorrow(user)) {
            throw LoanNotAllowedException(user.fineBalanceCents, BORROWING_FINE_LIMIT_IN_CENTS)
        }
        if (bookCopy.status != BookStatus.AVAILABLE) {
            throw LoanNotAllowedException(bookCopy.barcode)
        }

        // Create loan
        // Due at the end of the day, LOAN_LENGTH_DAYS # of days later
        val loan = Loan(
            user = user,
            bookCopy = bookCopy,
            dateBorrowed = LocalDateTime.now(),
            dateDue = LocalDateTime.of(LocalDate.now().plusDays(LOAN_LENGTH_DAYS.toLong()), LocalTime.MAX)
        )
        user.loans.add(loan)
        bookCopy.loan = loan
        bookCopy.status = BookStatus.BORROWED

        // Save entities - cascades from loan to user and availableCopy
        loanRepository.save(loan)
        return loan
    }

    override fun returnBook(loan: Loan) {
        val user = loan.user
        val bookCopy = loan.bookCopy

        // remove loan from associated entities
        user.loans.remove(loan)
        bookCopy.loan = null
        bookCopy.status = BookStatus.AVAILABLE

        userRepository.save(user)
        bookCopyRepository.save(bookCopy)
        loanRepository.delete(loan)
    }

    override fun renewBook(loan: Loan): Loan {
        // Check if user is allowed to renew the book
        if (!isUserEligibleToRenew(loan.user)) {
            throw RenewalNotAllowedException(loan.user.fineBalanceCents, RENEWAL_FINE_LIMIT_IN_CENTS)
        }
        if (loan.numRenewals >= MAX_NUM_RENEWALS) {
            throw RenewalNotAllowedException(loan.numRenewals)
        }

        // renew
        loan.dateDue = loan.dateDue.plusDays(LOAN_LENGTH_DAYS.toLong())
        loan.numRenewals += 1
        loanRepository.save(loan)

        return loan
    }

    override fun isUserEligibleToRenew(user: User): Boolean {
        return user.fineBalanceCents < RENEWAL_FINE_LIMIT_IN_CENTS
    }

    override fun isUserEligibleToBorrow(user: User): Boolean {
        return user.fineBalanceCents < BORROWING_FINE_LIMIT_IN_CENTS &&
            user.loans.size < MAX_NUM_BOOK_LOANS
    }
}