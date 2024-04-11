package io.duhanmo.kotlinjooq.jooq.member

import io.duhanmo.jooq.tables.records.MembersRecord
import java.time.LocalDateTime


data class Member(
    val id: Int? = null,
    val name: String? = null,
    val age: Int? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    constructor(record: MembersRecord) : this(
        id = record.id,
        name = record.name,
        age = record.age,
        createdAt = record.createdAt,
        updatedAt = record.updatedAt,
    )
}

