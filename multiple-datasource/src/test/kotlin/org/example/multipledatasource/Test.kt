package org.example.multipledatasource

import org.junit.jupiter.api.Test

class Test {

    @Test
    fun `몫 나머지 테스트`() {
        var count = 0
        for (i: Int in 35748 downTo 0 step(100)) {
            if (i > 100 && i % 5000 < 100) {
                println("$i")
                count++
            }

        }
        println(count)
    }
}