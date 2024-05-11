package io.duhanmo.distributedlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class DistributedlockApplication

fun main(args: Array<String>) {
    runApplication<DistributedlockApplication>(*args)
}
