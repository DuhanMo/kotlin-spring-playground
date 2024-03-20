package org.example.securitynormalcookie.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {
    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionController::class.java)
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResult> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResult(
                    code = "400",
                    message = ex.message,
                )
            )
    }

//     methodSecurity는 커스텀한 AccessDeniedHandler를 타지않는다.
//     403예외도 해당 어드바이스에서 처리해버려 500이 떨어지는 현상
    @ExceptionHandler(Exception::class)
    fun handleRuntimeException(ex: Exception): ResponseEntity<ErrorResult> {
        logger.error("ex", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResult(
                    code = "500",
                    message = ex.message,
                )
            )
    }
}