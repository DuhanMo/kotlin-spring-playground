package io.duhanmo.distributedlock.coupon

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

/**
 * 쿠폰
 */
@Entity
class Coupon(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
    val name: String,
    var availableStock: Long,
) {
    fun decrease() {
        validateStockCount()
        this.availableStock -= 1
    }

    private fun validateStockCount() {
        if (availableStock < 1) {
            throw IllegalArgumentException()
        }
    }
}