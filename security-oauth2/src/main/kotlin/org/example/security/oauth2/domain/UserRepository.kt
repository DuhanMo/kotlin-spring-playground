package org.example.security.oauth2.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByProviderAndSocialId(provider: SocialProvider, socialId: String): User?
}