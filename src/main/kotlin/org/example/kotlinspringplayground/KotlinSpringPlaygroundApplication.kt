package org.example.kotlinspringplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinSpringPlaygroundApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringPlaygroundApplication>(*args)
}
