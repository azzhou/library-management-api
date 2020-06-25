package com.example.library.service

import com.example.library.entity.User
import com.example.library.exception.UserNotFoundException
import com.example.library.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.shouldHaveThrown
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test_no_security")
class UserServiceTest(
    @Autowired
    val userService: UserService
) {
    @MockkBean
    lateinit var userRepository: UserRepository

    @Test
    fun `when getAllUsers then return list of User entities`() {
        val users = listOf(
            User("patrick@email.com", "Patrick", "Star", "password"),
            User("sandy@email.com", "Sandy", "Cheeks", "password")
        )
        val pageable = PageRequest.of(0, 100)
        every { userRepository.findAll(pageable) } returns PageImpl(users)

        assertThat(userService.getAllUsers(PageRequest.of(0, 100))).containsExactlyInAnyOrderElementsOf(users)
    }

    @Test
    fun `when getUserWithId on non-existent User then throw UserNotFoundException`() {
        every { userRepository.findByIdOrNull(-1) } returns null
        try {
            userService.getUserWithId(-1)
            shouldHaveThrown<Any>(UserNotFoundException::class.java)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(UserNotFoundException::class.java)
        }
    }

}