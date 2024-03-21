package org.example.security.oauth2.domain

interface OAuth2AuthCodeUrlProviderStrategy {
    val support: SocialProvider

    fun provide(state: String?): String
}