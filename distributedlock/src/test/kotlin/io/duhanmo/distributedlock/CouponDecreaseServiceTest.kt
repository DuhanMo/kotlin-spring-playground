package io.duhanmo.distributedlock

import io.duhanmo.distributedlock.coupon.Coupon
import io.duhanmo.distributedlock.coupon.CouponDecreaseService
import io.duhanmo.distributedlock.coupon.CouponRepository
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CouponDecreaseServiceTest(
    @Autowired private val couponRepository: CouponRepository,
    @Autowired private val couponDecreaseService: CouponDecreaseService,
) {
    @Test
    fun `쿠폰 감소 분산락 O`() {
        val coupon = Coupon(name = "쿠폰001", availableStock = 100)
        couponRepository.save(coupon)
        val numberOfThreads = 100
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    couponDecreaseService.decreaseWithLock(coupon.name, coupon.id!!)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val savedCoupon = couponRepository.findByIdOrNull(coupon.id) ?: throw IllegalArgumentException()
        println("잔여 쿠폰 개수: ${savedCoupon.availableStock}")
        assertThat(savedCoupon.availableStock).isZero()
    }

    @Test
    fun `쿠폰 감소 분산락 X`() {
        val coupon = Coupon(name = "쿠폰001", availableStock = 100)
        couponRepository.save(coupon)
        val numberOfThreads = 100
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    couponDecreaseService.decrease(coupon.id!!)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val savedCoupon = couponRepository.findByIdOrNull(coupon.id) ?: throw IllegalArgumentException()
        println("잔여 쿠폰 개수: ${savedCoupon.availableStock}")
        assertThat(savedCoupon.availableStock).isZero()
    }
}