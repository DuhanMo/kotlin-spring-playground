package org.example.security.oauth2.service

import org.example.security.oauth2.domain.OAuth2AuthCodeUrlProviderContext
import org.example.security.oauth2.domain.OAuth2UserClientContext
import org.example.security.oauth2.domain.SocialProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2Service(
    private val oAuth2AuthCodeUrlProviderContext: OAuth2AuthCodeUrlProviderContext,
    private val oAuth2UserClientContext: OAuth2UserClientContext
) {
    fun getRedirectUrl(provider: SocialProvider, state: String?): String {
        return oAuth2AuthCodeUrlProviderContext.getRedirectUrl(provider, state)
    }

    @Transactional
    fun login(provider: SocialProvider, code: String) {
        val oAuth2UserData = oAuth2UserClientContext.getOAuth2UserData(provider, code)
    }
}