package org.example.securitynormalcookie.domain

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class UserPrincipal(
    user: org.example.securitynormalcookie.domain.User
) : User(
    user.email, user.password, listOf(SimpleGrantedAuthority("ROLE_ADMIN"))
) {
    val userId: Long = user.id!!
}