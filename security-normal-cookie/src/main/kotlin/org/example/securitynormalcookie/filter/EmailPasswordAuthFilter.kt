package org.example.securitynormalcookie.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class EmailPasswordAuthFilter(
    url: String,
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter(url) {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val emailPassword = objectMapper.readValue(request?.inputStream, EmailPassword::class.java)

        val token = UsernamePasswordAuthenticationToken.unauthenticated(
            emailPassword.email,
            emailPassword.password
        )
        token.details = authenticationDetailsSource.buildDetails(request)
        return authenticationManager.authenticate(token)
    }
}

data class EmailPassword(val email: String, val password: String)

