package io.duhanmo.distributedlock

import io.duhanmo.distributedlock.purchase.PurchaseRepository
import io.duhanmo.distributedlock.purchase.PurchaseService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private const val 발주_코드 = "발주코드001"

@SpringBootTest
class PurchaseServiceTest(
    @Autowired val purchaseService: PurchaseService,
    @Autowired val purchaseRepository: PurchaseRepository,
) {
    @Test
    fun `발주등록 분산락 O`() {
        val numberOfThreads = 10
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    purchaseService.registerWithLock(lockName = 발주_코드, code = 발주_코드)
                } finally {
                    latch.countDown()
                }
            }

        }
        latch.await()

        val purchaseCount = purchaseRepository.countByCode(발주_코드)
        println("[발주코드001]로 등록된 발주 개수: $purchaseCount")
        assertThat(purchaseCount).isOne()
    }

    @Test
    fun `발주등록 분산락 X`() {
        val numberOfThreads = 10
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    purchaseService.register(code = 발주_코드)
                } finally {
                    latch.countDown()
                }
            }

        }

        latch.await()

        val purchaseCount = purchaseRepository.countByCode(발주_코드)
        println("[발주코드001]로 등록된 발주 개수: $purchaseCount")
        assertThat(purchaseCount).isEqualTo(1)
    }
}