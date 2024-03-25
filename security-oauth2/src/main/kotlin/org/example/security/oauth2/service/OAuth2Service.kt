package org.example.security.oauth2.service

import org.example.security.oauth2.domain.OAuth2AuthCodeUrlProviderContext
import org.example.security.oauth2.domain.OAuth2UserClientContext
import org.example.security.oauth2.domain.SocialProvider
import org.example.security.oauth2.domain.User
import org.example.security.oauth2.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2Service(
    private val oAuth2AuthCodeUrlProviderContext: OAuth2AuthCodeUrlProviderContext,
    private val oAuth2UserClientContext: OAuth2UserClientContext,
    private val userRepository: UserRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(OAuth2Service::class.java)
    }

    fun getRedirectUrl(provider: SocialProvider, state: String?): String {
        return oAuth2AuthCodeUrlProviderContext.getRedirectUrl(provider, state)
    }

    @Transactional
    fun login(provider: SocialProvider, code: String) {
        val oAuth2UserData = oAuth2UserClientContext.getOAuth2UserData(provider, code)
        logger.info("[OAuth2 Login] $oAuth2UserData")
    }

    @Transactional
    fun loadUserByProviderAndCode(provider: SocialProvider, code: String): UserPrincipal {
        println("OAuth2Service.loadUserByProviderAndCode 동작")
        val oAuth2UserData = oAuth2UserClientContext.getOAuth2UserData(provider, code)
        println("oAuth2UserData = ${oAuth2UserData}")
        // 최초 유저라면 회원가입
        val user =
            userRepository.findByProviderAndSocialId(oAuth2UserData.provider, oAuth2UserData.id)
                ?: userRepository.save(User.fromOAuth2(oAuth2UserData))
        return UserPrincipal(user)
    }
}

class UserPrincipal(
    user: User
) : UserDetails {
    val userId: Long = user.id!!

    // todo 추후 실제 유저역할 user.authorities로 수정
    override fun getAuthorities(): List<SimpleGrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_ADMIN"))

    override fun getPassword(): String? = null

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}