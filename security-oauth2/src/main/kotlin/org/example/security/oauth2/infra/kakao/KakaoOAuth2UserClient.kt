package org.example.security.oauth2.infra.kakao

import org.example.security.oauth2.domain.OAuth2UserClientStrategy
import org.example.security.oauth2.domain.OAuth2UserData
import org.example.security.oauth2.domain.SocialProvider
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class KakaoOAuth2UserClient(
    private val kakaoAuthApi: KakaoAuthApi,
    private val kakaoUserApi: KakaoUserApi,
    private val properties: KakaoOAuth2Properties
) : OAuth2UserClientStrategy {

    override val support: SocialProvider
        get() = SocialProvider.KAKAO

    override fun fetch(code: String): OAuth2UserData {
        val kakaoToken = kakaoAuthApi.fetchToken(tokenRequestParam(code))
        return kakaoUserApi.fetchUser(kakaoToken.accessToken)
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