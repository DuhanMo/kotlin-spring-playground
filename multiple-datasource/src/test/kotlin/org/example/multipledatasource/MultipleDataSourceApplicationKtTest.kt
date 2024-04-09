package org.example.multipledatasource

import org.example.multipledatasource.demo.DemoService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MultipleDataSourceApplicationTest(
    @Autowired private val demoService: DemoService
) {
    @Test
    fun writeTest() {
        demoService.save("데모5")
    }

    @Test
    fun readTest() {
        demoService.findAll()
    }
}