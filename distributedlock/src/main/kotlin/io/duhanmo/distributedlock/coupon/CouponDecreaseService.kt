package io.duhanmo.distributedlock.coupon

import io.duhanmo.distributedlock.DistributedLock
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponDecreaseService(
    private val couponRepository: CouponRepository,
) {
    @DistributedLock(key = "#lockName + '-' + #couponId")
    fun decreaseWithLock(lockName: String, couponId: Long) {
        val coupon = couponRepository.findByIdOrNull(couponId) ?: throw IllegalArgumentException()
        coupon.decrease()
    }

    @Transactional
    fun decrease(couponId: Long) {
        val coupon = couponRepository.findByIdOrNull(couponId) ?: throw IllegalArgumentException()
        coupon.decrease()
    }
}