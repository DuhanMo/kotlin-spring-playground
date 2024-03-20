package org.example.securitynormalcookie.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping("/")
    fun home(): String {
        return "home 입니다 🏠"
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun user(): String {
        return "user 접근 가능👁"
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun admin(): String {
        return "admin 접근 가능 👨‍💼"
    }
}