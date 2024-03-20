package org.example.securitynormalcookie.config.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.securitynormalcookie.domain.UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class LoginSuccessHandler : AuthenticationSuccessHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginSuccessHandler::class.java)
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val userPrincipal = authentication?.principal as UserPrincipal
        logger.info("[인증성공] user=${userPrincipal.username}")

        response?.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = Charsets.UTF_8.name()
            status = HttpStatus.OK.value()
        }
    }
}