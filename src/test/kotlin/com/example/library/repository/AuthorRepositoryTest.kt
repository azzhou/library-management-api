package com.example.library.repository

import com.example.library.entity.Author
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest @Autowired constructor(
    // use EntityManager for setup when testing Repository since the latter abstracts the former
    val entityManager: TestEntityManager,
    val authorRepository: AuthorRepository
) {

    @Test
    fun `when findByLastNameIgnoreCase then return Authors whose first or last name matches the search term`() {
        val smithAuthors = listOf(
            Author("Joe", "Smith"),
            Author("Jane", "Smith"),
            Author("John", "Smith")
        )
        val otherAuthors = listOf(
            Author("Smith", "Joe"),
            Author("Christine", "Feehan"),
            Author("Lee", "Child"),
            Author("John", "Sandford"),
            Author("Stuart", "Woods")
        )
        (smithAuthors + otherAuthors).map { entityManager.persist(it) }
        entityManager.flush()

        val foundAuthors = authorRepository.findByLastNameIgnoreCase("smItH", PageRequest.of(0, 100))
        assertThat(foundAuthors).containsExactlyInAnyOrderElementsOf(smithAuthors)
    }

    @Test
    fun `when findByLastNameIgnoreCase on nonexistent last name then return empty list`() {
        val authors = listOf(
            Author("Joe", "Smith"),
            Author("Jane", "Smith"),
            Author("John", "Smith"),
            Author("Smith", "Joe"),
            Author("Christine", "Feehan"),
            Author("Lee", "Child"),
            Author("John", "Sandford"),
            Author("Stuart", "Woods")
        )
        authors.map { entityManager.persist(it) }
        entityManager.flush()

        val foundAuthors = authorRepository.findByLastNameIgnoreCase("notalastname",
            PageRequest.of(0, 100))
        assertThat(foundAuthors.toList().size).isEqualTo(0)
    }

    @Test
    fun `when findByFirstNameAndLastNameAllIgnoreCase then return Authors matching those names`() {
        val leeChild = listOf(
            Author("Lee", "Child"),
            Author("Lee", "Child"),
            Author("Lee", "Child")
        )
        val authors = listOf(
            Author("Christine", "Feehan"),
            Author("John", "Sandford"),
            Author("Stuart", "Woods")
        )
        (leeChild + authors).map { entityManager.persist(it) }
        entityManager.flush()

        val foundAuthors = authorRepository.findByFirstNameAndLastNameAllIgnoreCase("lee", "child",
            PageRequest.of(0, 100))
        assertThat(foundAuthors).containsExactlyInAnyOrderElementsOf(leeChild)
    }
}