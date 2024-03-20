package org.example.securitynormalcookie.service

import org.example.securitynormalcookie.domain.User
import org.example.securitynormalcookie.domain.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(email: String, password: String) {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        userRepository.save(User(email, passwordEncoder.encode(password)))
    }
}