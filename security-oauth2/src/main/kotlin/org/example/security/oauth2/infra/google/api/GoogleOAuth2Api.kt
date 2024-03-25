package org.example.security.oauth2.infra.google.api

import org.example.security.oauth2.infra.google.dto.GoogleToken
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "google-oauth2-api", url = "https://oauth2.googleapis.com")
interface GoogleOAuth2Api {
    @PostMapping("/token")
    fun fetchToken(@RequestParam param: MultiValueMap<String, String>): GoogleToken
}