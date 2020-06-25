package com.example.library.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
// Disable for the "test_no_security" profile to test other layers without worrying about security
@Profile("!test_no_security")
class WebSecurityConfig(
    @Autowired
    private val jwtProvider: JwtProvider
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        // disable authentication requirement for these resources
        val ignoredPatternsPostRequests = arrayOf(
            "/users",
            "/users/token"
        )
        val ignoredPatternsGetRequests = arrayOf(
            "/books", "/books/**",
            "/authors", "/authors/**",
            "/book-copies", "/book-copies/**",
            "/api-docs**", "/api-docs/**",
            "/swagger-ui**", "/swagger-ui/**"
        )
        web.ignoring().mvcMatchers(HttpMethod.POST, *ignoredPatternsPostRequests)
        web.ignoring().mvcMatchers(HttpMethod.GET, *ignoredPatternsGetRequests)
    }

    override fun configure(http: HttpSecurity) {
        // disable cross site request forgery protection - no cookies
        http.csrf().disable()
        // disable creation and use sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // require authentication for all requests other than what is ignored in configure(web)
        http.authorizeRequests().anyRequest().authenticated()

        // add filter to default spring security filter chain
        val customFilter = JwtFilter(jwtProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(12)
    }
}