package org.example.security.oauth2.domain

import org.springframework.stereotype.Component

@Component
class OAuth2UserClientContext(
    strategies: Set<OAuth2UserClientStrategy>
) {
    private val strategies = strategies.associateBy { it.support }

    fun getOAuth2UserData(provider: SocialProvider, code: String): OAuth2UserData {
        return strategies[provider]
            ?.fetch(code)
            ?: throw IllegalArgumentException()
    }
}