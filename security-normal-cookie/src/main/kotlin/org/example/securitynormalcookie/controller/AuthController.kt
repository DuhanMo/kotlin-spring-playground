package org.example.securitynormalcookie.controller

import org.example.securitynormalcookie.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/api/auth/register")
    fun register(@RequestBody request: UserRegisterRequest): ResponseEntity<Any> {
        authService.register(request.email, request.password)
        return ResponseEntity.ok().build()
    }
}

data class UserRegisterRequest(val email: String, val password: String)