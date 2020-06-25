package com.example.library.repository

import com.example.library.entity.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class LoanRepositoryTest @Autowired constructor(
    // use EntityManager for setup when testing Repository since the latter abstracts the former
    val entityManager: TestEntityManager,
    val loanRepository: LoanRepository) {

    lateinit var user: User
    lateinit var book: Book

    @BeforeEach
    fun setup() {
        // create User and Book, which are used in all tests
        user = User("patrick@email.com", "Patrick", "Star", "password")
        book = Book("1234", "title", "publisher", "language")
        entityManager.persist(user)
        entityManager.persist(book)
        entityManager.flush()
    }

    @Test
    fun `when findByUser_idAndBookCopy_barcode on existing Loan then return Loan`() {
        val bookCopy = BookCopy(BookFormat.PAPERBACK, book)
        val loan = Loan(user, bookCopy, LocalDateTime.now(), LocalDateTime.MIN)
        entityManager.persistAndFlush(loan)
        val foundLoan = loanRepository.findByUser_idAndBookCopy_barcode(user.id, bookCopy.barcode)
        assertThat(foundLoan).isEqualTo(loan)
    }

    @Test
    fun `when findByUser_idAndBookCopy_barcode on non-existent userId then return null`() {
        val bookCopy = BookCopy(BookFormat.PAPERBACK, book)
        val loan = Loan(user, bookCopy, LocalDateTime.now(), LocalDateTime.MIN)
        entityManager.persistAndFlush(loan)
        val foundLoan = loanRepository.findByUser_idAndBookCopy_barcode(-1, bookCopy.barcode)
        assertThat(foundLoan).isEqualTo(null)
    }

    @Test
    fun `when findByUser_idAndBookCopy_barcode on non-existent barcode then return null`() {
        val bookCopy = BookCopy(BookFormat.PAPERBACK, book)
        val loan = Loan(user, bookCopy, LocalDateTime.now(), LocalDateTime.MIN)
        entityManager.persistAndFlush(loan)
        val foundLoan = loanRepository.findByUser_idAndBookCopy_barcode(user.id, -1)
        assertThat(foundLoan).isEqualTo(null)
    }

    @Test
    fun `when findByDateDueBefore with date then return list of loans overdue as of that date`() {
        val now = LocalDateTime.now()
        val overdueLoans = listOf(
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(5), now.minusDays(2)),
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(8), now.minusDays(4)),
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(40), now.minusDays(30))
        )
        val nonOverdueBooks = listOf(
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(4), now.plusDays(30)),
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(1), now.plusDays(3)),
            Loan(user, BookCopy(BookFormat.PAPERBACK, book), now.minusDays(12), now.plusDays(5))
        )
        (overdueLoans + nonOverdueBooks).map { entityManager.persist(it) }
        entityManager.flush()

        val foundLoans = loanRepository.findByDateDueBefore(now)
        assertThat(foundLoans).containsExactlyInAnyOrderElementsOf(overdueLoans)
    }

}