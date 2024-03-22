package org.example.security.oauth2.infra.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(value = "kakao-user-api", url = "https://kapi.kakao.com")
interface KakaoUserApi {
    @GetMapping("/v2/user/me", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun fetchUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) accessToken: String): KakaoUserResponse
}