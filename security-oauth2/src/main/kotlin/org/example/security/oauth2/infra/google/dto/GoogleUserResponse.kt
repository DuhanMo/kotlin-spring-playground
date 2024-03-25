package org.example.security.oauth2.infra.google.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.example.security.oauth2.domain.OAuth2UserData
import org.example.security.oauth2.domain.SocialProvider

@JsonNaming(SnakeCaseStrategy::class)
data class GoogleUserResponse(
    val id: String,
    val email: String,
    val name: String
) {
    fun toOAuth2UserData(): OAuth2UserData {
        return OAuth2UserData(SocialProvider.GOOGLE, id, name)
    }
}