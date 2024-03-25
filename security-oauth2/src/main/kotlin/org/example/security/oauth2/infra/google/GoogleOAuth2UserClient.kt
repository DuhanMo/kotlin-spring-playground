package org.example.security.oauth2.infra.google

import org.example.security.oauth2.domain.OAuth2UserClientStrategy
import org.example.security.oauth2.domain.OAuth2UserData
import org.example.security.oauth2.domain.SocialProvider
import org.example.security.oauth2.infra.google.api.GoogleApi
import org.example.security.oauth2.infra.google.api.GoogleOAuth2Api
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class GoogleOAuth2UserClient(
    private val googleOAuth2Api: GoogleOAuth2Api,
    private val googleApi: GoogleApi,
    private val properties: GoogleOAuth2Properties
) : OAuth2UserClientStrategy {
    override val support: SocialProvider
        get() = SocialProvider.GOOGLE

    override fun fetch(code: String): OAuth2UserData {
        val googleToken = googleOAuth2Api.fetchToken(tokenRequestParam(code))
        return googleApi.fetchUser("Bearer ${googleToken.accessToken}")
            .toOAuth2UserData()
    }

    private fun tokenRequestParam(code: String): MultiValueMap<String, String> {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", properties.clientId)
        params.add("client_secret", properties.clientSecret)
        params.add("code", code)
        params.add("redirect_uri", properties.redirectUri)
        return params
    }
}