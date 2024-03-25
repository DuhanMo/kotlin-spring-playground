package org.example.security.oauth2.infra.kakao.api

import org.example.security.oauth2.infra.kakao.dto.KakaoUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(value = "kakao-api", url = "https://kapi.kakao.com")
interface KakaoApi {
    @GetMapping("/v2/user/me", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun fetchUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) accessToken: String): KakaoUserResponse
}