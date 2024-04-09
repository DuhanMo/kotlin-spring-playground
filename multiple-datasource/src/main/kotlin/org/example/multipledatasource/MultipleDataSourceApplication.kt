package org.example.multipledatasource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MultipleDataSourceApplication

fun main(args: Array<String>) {
    runApplication<MultipleDataSourceApplication>(*args)
}