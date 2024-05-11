package io.duhanmo.distributedlock.purchase

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id

/**
 * 발주
 */
@Entity
class Purchase(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,

    val code: String,
) {
}