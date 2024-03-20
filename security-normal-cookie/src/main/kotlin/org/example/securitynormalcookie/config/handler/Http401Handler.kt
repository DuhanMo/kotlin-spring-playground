package org.example.securitynormalcookie.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.securitynormalcookie.controller.ErrorResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class Http401Handler(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(Http401Handler::class.java)

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger.error("[인증오류] 로그인이 필요합니다.")
        val errorResult = ErrorResult(
            code = "401",
            message = "로그인이 필요합니다."
        )
        response?.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = Charsets.UTF_8.name()
            status = HttpStatus.UNAUTHORIZED.value()
        }
        objectMapper.writeValue(response?.writer, errorResult)
    }
}