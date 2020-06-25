package com.example.library

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class TestConfiguration {

    // Disable security elements while still providing beans required in UserService
    @EnableWebSecurity
    @Profile("test_no_security")
    class TestWebSecurityConfig() : WebSecurityConfigurerAdapter() {

        @Bean
        override fun authenticationManagerBean(): AuthenticationManager {
            return super.authenticationManagerBean()
        }

        @Bean
        fun passwordEncoder(): PasswordEncoder {
            return BCryptPasswordEncoder(12)
        }
    }

}




