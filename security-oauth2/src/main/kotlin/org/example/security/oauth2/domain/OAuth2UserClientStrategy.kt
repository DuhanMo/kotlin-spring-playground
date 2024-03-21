package org.example.security.oauth2.domain

interface OAuth2UserClientStrategy {
    val support: SocialProvider

    fun fetch(code: String): OAuth2UserData
}