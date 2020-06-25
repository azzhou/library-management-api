package com.example.library.repository

import com.example.library.entity.Book
import com.example.library.entity.BookCopy
import com.example.library.entity.BookFormat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class BookCopyRepositoryTest @Autowired constructor(
    // use EntityManager for setup when testing Repository since the latter abstracts the former
    val entityManager: TestEntityManager,
    val bookCopyRepository: BookCopyRepository) {

    @Test
    fun `when findByIdOrNull on existing barcode then return BookCopy`() {
        val book = Book("1234", "title", "publisher", "language")
        val bookCopy = BookCopy(BookFormat.PAPERBACK, book)
        book.bookCopies.add(bookCopy)
        entityManager.persistAndFlush(book)

        val foundBookCopy = bookCopyRepository.findByIdOrNull(bookCopy.barcode)
        assertThat(foundBookCopy).isEqualTo(bookCopy)
    }

    @Test
    fun `when findByIdOrNull on nonexistent barcode then return null`() {
        val foundBookCopy = bookCopyRepository.findByIdOrNull(123092304)
        assertThat(foundBookCopy).isEqualTo(null)
    }
}