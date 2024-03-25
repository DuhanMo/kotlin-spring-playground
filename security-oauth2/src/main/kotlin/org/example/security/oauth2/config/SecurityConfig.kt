package org.example.security.oauth2.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.security.oauth2.service.OAuth2Service
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val oAuth2Service: OAuth2Service
) {
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/").permitAll()
                auth.requestMatchers("/oauth2/**").permitAll()
                auth.anyRequest().authenticated()
            }
            .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .addFilterBefore(oAuth2Filter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun oAuth2Filter(): OAuth2Filter {
        val filter = OAuth2Filter("/oauth2/login", objectMapper)
        filter.setAuthenticationManager(oAuth2authenticationManager())
        filter.setSecurityContextRepository(HttpSessionSecurityContextRepository())
        return filter
    }

    @Bean
    fun oAuth2authenticationManager(): AuthenticationManager {
        val oAuth2AuthenticateProvider = OAuth2AuthenticateProvider(oAuth2Service)
        return ProviderManager(oAuth2AuthenticateProvider)
    }
}