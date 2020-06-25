package com.example.library.service

import com.example.library.entity.*
import com.example.library.exception.InvalidFinePaymentException
import com.example.library.exception.UserNotFoundException
import com.example.library.repository.LoanRepository
import com.example.library.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.shouldHaveThrown
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test_no_security")
class FineServiceTest @Autowired constructor(
    val fineService: FineService
) {
    @MockkBean
    lateinit var userRepository: UserRepository
    @MockkBean
    lateinit var loanRepository: LoanRepository

    @Test
    fun `when payFine on non-existent User then throw UserNotFoundException`() {
        every { userRepository.findByIdOrNull(-1) } returns null
        try {
            fineService.payFine(-1, 100)
            shouldHaveThrown<Any>(UserNotFoundException::class.java)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(UserNotFoundException::class.java)
        }
    }

    @Test
    fun `when payFine on User with paymentAmount lte 0 then throw InvalidFinePaymentException`() {
        val user = User("email", "first", "last", "pass")
        user.fineBalanceCents = 200
        every { userRepository.findByIdOrNull(user.id) } returns user

        try {
            fineService.payFine(user.id, -30)
            shouldHaveThrown<Any>(InvalidFinePaymentException::class.java)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(InvalidFinePaymentException::class.java)
        }
    }

    @Test
    fun `when payFine on User with paymentAmount gt fineBalanceCents then throw InvalidFinePaymentException`() {
        val user = User("email", "first", "last", "pass")
        user.fineBalanceCents = 200
        every { userRepository.findByIdOrNull(user.id) } returns user

        try {
            fineService.payFine(user.id, 300)
            shouldHaveThrown<Any>(InvalidFinePaymentException::class.java)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(InvalidFinePaymentException::class.java)
        }
    }

    @Test
    fun `when payFine on User with valid paymentAmount then reduce fineBalance by that amount`() {
        val user = User("email", "first", "last", "pass")
        user.fineBalanceCents = 200
        every { userRepository.findByIdOrNull(user.id) } returns user
        every { userRepository.save(user) } returns user

        val updatedUser = fineService.payFine(user.id, 150)
        assertThat(updatedUser.fineBalanceCents).isEqualTo(50)
    }

    @Test
    fun `when updateFines then Users with overdue books have fines incremented`() {
        // Entities
        val book = Book("1234", "title", "publisher", "english")
        val user1 = User("email1", "first", "last", "pass")
        val user2 = User("email2", "first", "last", "pass")

        val now = LocalDateTime.now()
        val overdueLoans: MutableIterable<Loan> = mutableListOf(
            Loan(user1, BookCopy(BookFormat.HARDCOVER, book), now.minusDays(10), now.minusDays(1)),
            Loan(user1, BookCopy(BookFormat.HARDCOVER, book), now.minusDays(15), now.minusDays(4)),
            Loan(user1, BookCopy(BookFormat.HARDCOVER, book), now.minusDays(34), now.minusDays(20)),
            Loan(user2, BookCopy(BookFormat.HARDCOVER, book), now.minusDays(7), now.minusDays(1))
        )

        // Mock repository search
        every { loanRepository.findByDateDueBefore(any()) } returns overdueLoans

        fineService.updateFines()
        assertThat(user1.fineBalanceCents == FINE_PER_PERIOD_IN_CENTS * 3L)
        assertThat(user1.fineBalanceCents == FINE_PER_PERIOD_IN_CENTS * 1L)
    }
}