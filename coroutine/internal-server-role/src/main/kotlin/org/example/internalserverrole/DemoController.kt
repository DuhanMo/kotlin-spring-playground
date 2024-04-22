package org.example.internalserverrole

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class DemoController {
    @GetMapping("/internal/coroutine/{count}")
    fun internal(@PathVariable count: Int): String {
        val restClient = RestClient.create()
        val items: IntRange = 1..count
        val start = System.currentTimeMillis()
        runBlocking {
            withContext(Dispatchers.IO) {
                val deferreds = items.map {
                    async {
                        restClient.get()
                            .uri("http://localhost:8081/external/{id}", it)
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
        return "total time: ${end - start} ms"
    }

    @GetMapping("/internal/not-coroutine/{count}")
    fun internalNotCoroutine(@PathVariable count: Int): String {
        val restClient = RestClient.create()
        val items: IntRange = 1..count
        val start = System.currentTimeMillis()
        val results = items.map {
            restClient.get()
                .uri("http://localhost:8081/external/{id}", it)
                .retrieve()
                .body(String::class.java)
        }
        results.forEach { println(it) }
        val end = System.currentTimeMillis()
        println("total time: ${end - start}")
        return "total time: ${end - start} ms"
    }

}