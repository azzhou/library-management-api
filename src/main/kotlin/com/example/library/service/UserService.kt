package com.example.library.service

import com.example.library.dto.*
import com.example.library.entity.User
import com.example.library.entity.UserAuthentication
import com.example.library.entity.UserPermission
import com.example.library.security.AuthenticationDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize

interface UserService {

    @Secured("ROLE_ADMIN", "ROLE_LIBRARIAN")
    fun getAllUsers(pageable: Pageable): Page<User>

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #userId == authentication.principal.id")
    fun getUserWithId(userId: Long): User

    fun registerNewUser(userRegistration: UserRegistration): UserAuthentication

    fun login(email: String, password: String): UserAuthentication

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN') or #user.id == authentication.principal.id")
    fun updateAccountInfo(user: User, userUpdate: UserUpdate): User

    @Secured("ROLE_ADMIN")
    fun updateUserPermission(user: User, newPermission: UserPermission): User

}