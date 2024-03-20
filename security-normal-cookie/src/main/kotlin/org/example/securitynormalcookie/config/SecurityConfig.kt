package org.example.securitynormalcookie.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.securitynormalcookie.config.handler.Http401Handler
import org.example.securitynormalcookie.config.handler.Http403Handler
import org.example.securitynormalcookie.config.handler.LoginFailureHandler
import org.example.securitynormalcookie.config.handler.LoginSuccessHandler
import org.example.securitynormalcookie.domain.UserPrincipal
import org.example.securitynormalcookie.domain.UserRepository
import org.example.securitynormalcookie.filter.EmailPasswordAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository
) {
    /*
    * 기본 에러페이지인 /error에 접근할 수 있게 설정.
    * ControllerAdvice를 통해 예외를 핸들링 한다면 /error페이지를 찾지 않기 때문에 해당 설정이 필요없음
    * */
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
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
            .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { e ->
                e.accessDeniedHandler(Http403Handler(objectMapper))
                e.authenticationEntryPoint(Http401Handler(objectMapper))
            }
            .build()
    }

    @Bean
    fun emailPasswordAuthFilter(): EmailPasswordAuthFilter {
        val filter = EmailPasswordAuthFilter("/api/auth/login", objectMapper)
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(LoginSuccessHandler())
        filter.setAuthenticationFailureHandler(LoginFailureHandler(objectMapper)) // 설정하지 않으면 기본핸들러가 동작, 커스텀한 디테일서비스에서 예외를 발생해도 무조건 401로 감싸서 응답
        filter.setSecurityContextRepository(HttpSessionSecurityContextRepository()) // 명시하지 않아도 동작하기는 함

        return filter
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService(userRepository))
        provider.setPasswordEncoder(passwordEncoder())
        return ProviderManager(provider)
    }

    /**
     *  UserDetailsService 빈만 단독으로 등록해놓으면 해당 빈을 시큐리티 인증에 사용한다.
     *  formLogin을 활성화 & UserDetailsService 등록 -> 폼 로그인 시 자동으로 email을 통해 인증 가능
     */
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { username ->
            val user =
                userRepository.findByEmail(username) ?: throw UsernameNotFoundException("${username}를 찾을 수 없습니다.")
            UserPrincipal(user)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}