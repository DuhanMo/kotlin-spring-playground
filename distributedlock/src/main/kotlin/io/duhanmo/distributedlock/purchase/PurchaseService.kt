package io.duhanmo.distributedlock.purchase

import io.duhanmo.distributedlock.DistributedLock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
) {
    @DistributedLock(key = "#lockName")
    fun registerWithLock(lockName: String, code: String) {
        if (purchaseRepository.existsByCode(code)) {
            throw IllegalArgumentException("이미 발주완료")
        }
        purchaseRepository.save(
            Purchase(code = code)
        )
    }

    @Transactional
    fun register(code: String) {
        if (purchaseRepository.existsByCode(code)) {
            throw IllegalArgumentException("이미 발주완료")
        }
        purchaseRepository.save(
            Purchase(code = code)
        )
    }
}