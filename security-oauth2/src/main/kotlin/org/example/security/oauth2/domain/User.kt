package org.example.security.oauth2.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User(
    private val username: String
) {
    companion object {
        fun fromOAuth2(data: OAuth2UserData): User {
            return User(data.nickname).apply {
                provider = data.provider
                socialId = data.id
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    var provider: SocialProvider? = null

    var socialId: String? = null
}