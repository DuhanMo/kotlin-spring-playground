package org.example.security.oauth2.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.security.oauth2.domain.SocialProvider
import org.example.security.oauth2.service.OAuth2Service
import org.example.security.oauth2.service.UserPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class OAuth2Filter(
    url: String,
    private val objectMapper: ObjectMapper
) : AbstractAuthenticationProcessingFilter(url) {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        println("OAuth2Filter.attemptAuthentication 동작")
        val providerCode = objectMapper.readValue(request?.inputStream, SocialProviderCode::class.java)
        val authenticateToken = OAuth2AuthenticateToken.unauthenticated(providerCode.provider, providerCode.code)
        authenticateToken.details = authenticationDetailsSource.buildDetails(request)
        return authenticationManager.authenticate(authenticateToken)
    }
}

data class SocialProviderCode(val provider: SocialProvider, val code: String)

class OAuth2AuthenticateToken(
    val provider: SocialProvider?,
    val code: String?,
    private val userPrincipal: UserPrincipal?,
    authorities: Collection<GrantedAuthority>?
) : AbstractAuthenticationToken(authorities) {
    companion object {
        fun unauthenticated(provider: SocialProvider, code: String): OAuth2AuthenticateToken {
            return OAuth2AuthenticateToken(provider, code, null, null)
        }

        fun authenticated(userPrincipal: UserPrincipal): OAuth2AuthenticateToken {
            val authenticateToken = OAuth2AuthenticateToken(null, null, userPrincipal, userPrincipal.authorities)
            authenticateToken.isAuthenticated = true
            authenticateToken.details = userPrincipal.authorities
            return authenticateToken
        }
    }

    override fun getCredentials(): Any? {
        println("OAuth2AuthenticateToken.getCredentials 동작")
        return null
    }

    override fun getPrincipal(): Any? {
        println("OAuth2AuthenticateToken.getPrincipal 동작")
        return userPrincipal?.userId
    }
}


class OAuth2AuthenticateProvider(
    private val oAuth2Service: OAuth2Service
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        println("OAuth2AuthenticateProvider.authenticate 동작")
        val authenticationToken = authentication as OAuth2AuthenticateToken
        val userPrincipal =
            oAuth2Service.loadUserByProviderAndCode(authenticationToken.provider!!, authenticationToken.code!!)
        return OAuth2AuthenticateToken.authenticated(userPrincipal)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication?.isAssignableFrom(OAuth2AuthenticateToken::class.java)
            ?: false
    }

}