package com.example.library.repository

import com.example.library.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest @Autowired constructor(
    // use EntityManager for setup when testing Repository since the latter abstracts the former
    val entityManager: TestEntityManager,
    val userRepository: UserRepository) {

    @Test
    fun `when findByIdOrNull on existing id then return User`() {
        val patrick = User("patrick@email.com", "Patrick", "Star", "password")
        entityManager.persistAndFlush(patrick)
        val user = userRepository.findByIdOrNull(patrick.id)
        assertThat(user).isEqualTo(patrick)
    }

    @Test
    fun `when findByIdOrNull on nonexistent id then return null`() {
        val user = userRepository.findByIdOrNull(1)
        assertThat(user).isEqualTo(null)
    }

    @Test
    fun `when findByEmailIgnoreCase on existing email then return User`() {
        val sandy = User("sAndY@EmaIl.com", "Sandy", "Cheeks", "password")
        entityManager.persist(sandy)
        entityManager.flush()
        val user = userRepository.findByEmailIgnoreCase(sandy.email)
        assertThat(user).isEqualTo(sandy)
    }

    @Test
    fun `when findByEmailIgnoreCase on nonexistent email then return null`() {
        val user = userRepository.findByEmailIgnoreCase("not@real.email")
        assertThat(user).isEqualTo(null)
    }
}