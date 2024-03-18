package org.example.kotlinspringplayground

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.client.RestClient

@SpringBootApplication
class CoroutineApplication

fun main(args: Array<String>) {
    runApplication<CoroutineApplication>(*args)
    val restClient = RestClient.create()
    val items: IntRange = 1..150
    val start = System.currentTimeMillis()
    runBlocking {
        withContext(Dispatchers.IO) {
            val deferreds = items.map {
                async {
                    restClient.get()
                        .uri("http://localhost:8081/{id}", it)
                        .retrieve()
                        .body(String::class.java)
                }
            }
            val awaitAll = deferreds.awaitAll()
            awaitAll.forEach { println(it) }
        }
    }
    val end = System.currentTimeMillis()
    println("total time: ${end - start}")
}
