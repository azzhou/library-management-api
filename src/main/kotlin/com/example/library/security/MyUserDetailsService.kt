package com.example.library.security

import com.example.library.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmailIgnoreCase(email)
            ?: throw UsernameNotFoundException("User with email $email not found")
        val authorities = arrayListOf(SimpleGrantedAuthority("ROLE_" + user.permission.name))
        return CurrentUser(user.id, user.email, user.password, authorities)
    }

}