package org.example.security.oauth2.domain

data class OAuth2UserData(val provider: SocialProvider, val id: String, val nickname: String)
