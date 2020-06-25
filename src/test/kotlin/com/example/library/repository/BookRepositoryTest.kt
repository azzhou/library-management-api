package com.example.library.repository

import com.example.library.entity.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest @Autowired constructor(
    // use EntityManager for setup when testing Repository since the latter abstracts the former
    val entityManager: TestEntityManager,
    val bookRepository: BookRepository
) {

    @Test
    fun `when findByIdOrNull on existing ISBN then return Book`() {
        val book = Book("1234", "title", "publisher", "language")
        entityManager.persistAndFlush(book)

        val foundBook = bookRepository.findByIdOrNull(book.isbn)
        assertThat(foundBook).isEqualTo(book)
    }

    @Test
    fun `when findByIdOrNull on nonexistent ISBN then return null`() {
        val book = Book("1234", "title", "publisher", "language")
        entityManager.persistAndFlush(book)

        val foundBook = bookRepository.findByIdOrNull("fake isbn")
        assertThat(foundBook).isEqualTo(null)
    }

    @Test
    fun `when findByTitleIgnoreCaseContaining then return Books whose titles contain the search term`() {
        val titlesContainingPotter = listOf(
            "Harry Potter and the Sorcerer's Stone",
            "Harry Potter and the Chamber of Secrets",
            "Harry Potter and the Goblet of Fire",
            "Potter Blah Blah",
            "Blah Blah Potter",
            "Blah pOttEr Blah"
        )
        val otherTitles = listOf(
            "A Time to Kill",
            "The House of Mirth",
            "East of Eden",
            "The Sun Also Rises",
            "Vile Bodies",
            "A Scanner Darkly",
            "Brave New World"
        )
        (titlesContainingPotter + otherTitles).mapIndexed { idx, title ->
            entityManager.persist(Book(idx.toString(), title, "publisher", "language"))
        }
        entityManager.flush()

        val foundBooks = bookRepository.findByTitleIgnoreCaseContaining("potter",
            PageRequest.of(0, 100))
        assertThat(foundBooks.map { it.title }).containsExactlyInAnyOrderElementsOf(titlesContainingPotter)
    }

    @Test
    fun `when findByTitleIgnoreCaseContaining with search term not in any titles then return empty list`() {
        val titles = listOf(
            "Harry Potter and the Sorcerer's Stone",
            "Harry Potter and the Chamber of Secrets",
            "Harry Potter and the Goblet of Fire",
            "A Time to Kill",
            "The House of Mirth",
            "East of Eden",
            "The Sun Also Rises",
            "Vile Bodies",
            "A Scanner Darkly",
            "Brave New World"
        )
        titles.mapIndexed { idx, title ->
            entityManager.persist(Book(idx.toString(), title, "publisher", "language"))
        }
        entityManager.flush()

        val foundBooks = bookRepository.findByTitleIgnoreCaseContaining("not in any of the titles",
            PageRequest.of(0, 100))
        assertThat(foundBooks.toList().size).isEqualTo(0)
    }
}