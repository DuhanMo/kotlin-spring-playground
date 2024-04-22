package org.example.externalserverrole

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {
    @GetMapping("/external/{id}")
    fun external(@PathVariable id: Int): String {
        Thread.sleep(100)
        println("id = $id")
        return "external response $id"
    }
}