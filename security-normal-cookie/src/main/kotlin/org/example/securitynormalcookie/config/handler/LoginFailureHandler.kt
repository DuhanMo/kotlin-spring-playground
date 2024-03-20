package org.example.securitynormalcookie.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.securitynormalcookie.controller.ErrorResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import kotlin.text.Charsets.UTF_8

class LoginFailureHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationFailureHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(LoginFailureHandler::class.java)
    }

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        logger.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.")
        val errorResult = ErrorResult(
            code = "400",
            message = "아이디 혹은 비밀번호가 올바르지 않습니다."
        )
        response?.apply {
            contentType = APPLICATION_JSON_VALUE
            characterEncoding = UTF_8.name()
            status = HttpStatus.BAD_REQUEST.value()
        }
        objectMapper.writeValue(response?.writer, errorResult)
    }
}