package org.example.security.oauth2.controller

import org.example.security.oauth2.domain.SocialProvider

data class OAuth2LoginRequest(val provider: SocialProvider, val code: String)
