package org.example.securitynormalcookie.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.securitynormalcookie.controller.ErrorResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class Http403Handler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(Http403Handler::class.java)
    }

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        logger.error("[인가오류] 403")

        val errorResult = ErrorResult(
            code = "403",
            message = "접근할 수 없습니다."
        )
        response?.apply {
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = Charsets.UTF_8.name()
            status = HttpStatus.FORBIDDEN.value()
        }
        objectMapper.writeValue(response?.writer, errorResult)
    }
}