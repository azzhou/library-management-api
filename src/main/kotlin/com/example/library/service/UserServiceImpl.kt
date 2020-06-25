package com.example.library.service

import com.example.library.dto.UserRegistration
import com.example.library.dto.UserUpdate
import com.example.library.dto.asEntity
import com.example.library.entity.User
import com.example.library.entity.UserAuthentication
import com.example.library.entity.UserPermission
import com.example.library.exception.UserAlreadyExistsException
import com.example.library.exception.UserNotFoundException
import com.example.library.repository.UserRepository
import com.example.library.security.AuthenticationDetails
import com.example.library.security.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getAllUsers(pageable: Pageable): Page<User> {
        return userRepository.findAll(pageable)
    }

    override fun getUserWithId(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException(userId)
    }

    override fun registerNewUser(userRegistration: UserRegistration): UserAuthentication {
        val email = userRegistration.email
        if (userRepository.findByEmailIgnoreCase(email) != null) {
            throw UserAlreadyExistsException(email)
        }
        val user = userRegistration.asEntity()
        user.password = passwordEncoder.encode(user.password)
        userRepository.save(user)
        return UserAuthentication(user, jwtProvider.createToken(email))
    }

    override fun login(email: String, password: String): UserAuthentication {
        // AuthenticationException is handled in ExceptionControllerAdvice
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

        // Only run if authentication above passes, so user is guaranteed to exist at this point
        val user = userRepository.findByEmailIgnoreCase(email)
        return UserAuthentication(user!!, jwtProvider.createToken(email))
    }

    override fun updateAccountInfo(user: User, userUpdate: UserUpdate): User {
        // If a new email is passed in, make sure it is unique or equal to the current email before updating
        userUpdate.email?.let{
            if (user.email != it && userRepository.findByEmailIgnoreCase(it) != null) {
                throw UserAlreadyExistsException(it)
            }
            user.email = it
        }

        userUpdate.firstName?.let { user.firstName = it }
        userUpdate.lastName?.let { user.lastName = it }
        userUpdate.password?.let { user.password = passwordEncoder.encode(it) }

        userRepository.save(user)
        return user
    }

    override fun updateUserPermission(user: User, newPermission: UserPermission): User {
        user.permission = newPermission
        userRepository.save(user)
        return user
    }
}