package org.example.security.oauth2.controller

import jakarta.servlet.http.HttpServletResponse
import org.example.security.oauth2.config.AppProperties
import org.example.security.oauth2.domain.SocialProvider
import org.example.security.oauth2.service.OAuth2Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class OAuth2Controller(
    private val oAuth2Service: OAuth2Service,
    private val appProperties: AppProperties
) {
    @GetMapping("/code/{provider}")
    fun redirectGetAuthCodePage(
        @PathVariable provider: SocialProvider,
        state: String?,
        response: HttpServletResponse
    ) {
        val targetUrl = oAuth2Service.getRedirectUrl(provider, state)
        response.sendRedirect(targetUrl)
    }

    @GetMapping("/callback/{provider}")
    fun redirectCodeAndStateToClient(
        @PathVariable provider: SocialProvider,
        code: String,
        state: String?,
        response: HttpServletResponse
    ) {
        response.sendRedirect(getTargetUrl(code, state, provider))
    }

    //    @PostMapping("/login")
    fun login(@RequestBody request: OAuth2LoginRequest) {
        oAuth2Service.login(request.provider, request.code)
    }

    private fun getTargetUrl(code: String, state: String?, provider: SocialProvider) =
        "${appProperties.clientBaseUrl}?code=$code&state=${state ?: ""}&provider=$provider"
}