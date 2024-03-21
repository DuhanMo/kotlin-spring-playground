package org.example.security.oauth2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableFeignClients
class SecurityOAuth2Application

fun main(args: Array<String>) {
    runApplication<SecurityOAuth2Application>(*args)
}