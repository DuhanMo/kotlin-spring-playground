package org.example.security.oauth2.infra.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "kakao-auth-api", url = "https://kauth.kakao.com")
interface KakaoAuthApi {
    @PostMapping("/oauth/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun fetchToken(@RequestParam param: MultiValueMap<String, String>): KakaoToken
}