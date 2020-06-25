package com.example.library.api

import com.example.library.entity.User
import com.example.library.exception.UserAlreadyExistsException
import com.example.library.exception.UserNotFoundException
import com.example.library.repository.UserRepository
import com.example.library.security.JwtProvider
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserApiTest @Autowired constructor(
    val userRepository: UserRepository,
    val mvc: MockMvc,
    val jwtProvider: JwtProvider
) {
    lateinit var testUsers: MutableList<User>

    @BeforeAll
    fun setup() {
        testUsers = mutableListOf(
            userRepository.save(User("patrick@email.com", "Patrick", "Star", "password")),
            userRepository.save(User("sandy@email.com", "Sandy", "Cheeks", "password"))
        )
    }

    @Test
    fun `when getUserWithId on nonexistent id then return 404 status and error details`() {
        val nonexistentId = testUsers.size + 1
        mvc.perform(get("/users/{id}", nonexistentId))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value(UserNotFoundException::class.java.simpleName))
    }

    @Test
    fun `when getUserWithId on existing id then return 200 status and user details`() {
        val existingId = 1
        mvc.perform(get("/users/{id}", existingId))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.email").value(testUsers[existingId - 1].email))
    }

    @Test
    fun `when addUser with unique email then return 201 status and valid JWT`() {
        val userJson = """
            {
                "email":"plankton@chumbucket.com",
                "firstName":"Sheldon",
                "lastName":"Plankton",
                "password":"password"
            }
        """.trimIndent()
        val result = mvc.perform(post("/users").contentType("application/json").content(userJson))
            .andExpect(status().isCreated)
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andReturn()

        val token = result.response.contentAsString
        assert(jwtProvider.validateToken(token))
    }

    @Test
    fun `when addUser with duplicate email then return 409 status and error details`() {
        val userJson = """
            {
                "email":"${testUsers[0].email}",
                "firstName":"Sheldon",
                "lastName":"Plankton",
                "password":"password"
            }
        """.trimIndent()
        mvc.perform(post("/users").contentType("application/json").content(userJson))
            .andExpect(status().isConflict)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.type").value(UserAlreadyExistsException::class.java.simpleName))
    }
}


