package io.duhanmo.distributedlock.purchase

import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRepository: JpaRepository<Purchase, Long> {
    fun existsByCode(code: String): Boolean
    fun countByCode(code: String): Long
}