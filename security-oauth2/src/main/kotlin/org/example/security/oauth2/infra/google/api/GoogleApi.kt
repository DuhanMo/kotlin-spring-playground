package org.example.security.oauth2.infra.google.api

import org.example.security.oauth2.infra.google.dto.GoogleUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(value = "google-api", url = "https://www.googleapis.com")
interface GoogleApi {
    @GetMapping("/oauth2/v2/userinfo")
    fun fetchUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) accessToken: String): GoogleUserResponse
}