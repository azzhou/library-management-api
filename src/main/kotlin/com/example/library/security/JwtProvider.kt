package com.example.library.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.servlet.http.HttpServletRequest


@Component
class JwtProvider(
    @Autowired
    private val userDetailsService: MyUserDetailsService
) {
    private val secretKey: String = System.getenv("LIBRARY_SECRET_KEY")
    private val validityInMilliseconds: Long = 3600000  // 1hr
    private val encodedKey: String

    init {
        encodedKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createToken(email: String): AuthenticationDetails {
        val claims = Jwts.claims().setSubject(email)
        // Jwts requires old Java Date object
        val issuedAtDate = Date()
        val expiresAtDate = Date(issuedAtDate.time + validityInMilliseconds)
        val token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAtDate)
            .setExpiration(expiresAtDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

        // Convert Date to LocalDateTime, which is newer and is used throughout the rest of the app
        val expiresAtLocalDateTime = LocalDateTime.ofInstant(expiresAtDate.toInstant(), ZoneId.systemDefault())

        return AuthenticationDetails(token, expiresAtLocalDateTime)
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        // extract token string from bearer token - header content has this form: 'Authorization: Bearer <token>'
        val bearerToken: String? = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length)
        }
        return null
    }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}