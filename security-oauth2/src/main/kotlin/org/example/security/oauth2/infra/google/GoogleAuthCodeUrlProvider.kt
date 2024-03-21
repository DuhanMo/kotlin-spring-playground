package org.example.security.oauth2.infra.google

import org.example.security.oauth2.domain.OAuth2AuthCodeUrlProviderStrategy
import org.example.security.oauth2.domain.SocialProvider
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class GoogleAuthCodeUrlProvider(
    private val properties: GoogleOAuth2Properties
) : OAuth2AuthCodeUrlProviderStrategy {
    override val support: SocialProvider
        get() = SocialProvider.GOOGLE

    override fun provide(state: String?): String {
        return UriComponentsBuilder
            .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId)
            .queryParam("redirect_uri", properties.redirectUri)
            .queryParam("scope", properties.scope.joinToString(" "))
            .queryParam("state", state)
            .build()
            .toUriString()
    }
}