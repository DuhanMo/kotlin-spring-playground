package org.example.security.oauth2.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class AppProperties(val clientBaseUrl: String)