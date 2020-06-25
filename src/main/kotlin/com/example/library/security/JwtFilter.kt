package com.example.library.security

import com.example.library.exception.ErrorDetails
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = jwtProvider.resolveToken(request)
        if (jwtProvider.validateToken(token)) {
            // token reflects a Claims JWS and is not null
            SecurityContextHolder.getContext().authentication = jwtProvider.getAuthentication(token!!)
            filterChain.doFilter(request, response)
        } else {
            SecurityContextHolder.clearContext()
            sendErrorJson(request, response)
        }
    }

    // Filtering is done before controller advice is active, so need additional method to send the error response
    private fun sendErrorJson(request: HttpServletRequest, response: HttpServletResponse) {
        val statusCode = HttpServletResponse.SC_UNAUTHORIZED
        val errorDetails = ErrorDetails(
            statusCode,
            mutableListOf("AuthenticationError"),
            "Security token is invalid, expired, or missing from the request header",
            request.requestURI,
            request.method
        )

        // Use ObjectMapper with time module to support one-line LocalDateTime serialization
        // Default mapper in RestController responses handles time serialization this way, but need it explicitly here
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        // set error status and details and commit response
        response.let {
            it.resetBuffer()
            it.status = statusCode
            it.setHeader("Content-Type", "application/json")
            it.outputStream.print(mapper.writeValueAsString(errorDetails))
            it.flushBuffer()
        }
    }
}
