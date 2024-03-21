package org.example.security.oauth2.domain

import org.springframework.stereotype.Component

@Component
class OAuth2AuthCodeUrlProviderContext(
    strategies: Set<OAuth2AuthCodeUrlProviderStrategy>
) {
    private val strategies = strategies.associateBy { it.support }

    fun getRedirectUrl(provider: SocialProvider, state: String?): String {
        return strategies[provider]
            ?.provide(state)
            ?: throw IllegalStateException()
    }
}