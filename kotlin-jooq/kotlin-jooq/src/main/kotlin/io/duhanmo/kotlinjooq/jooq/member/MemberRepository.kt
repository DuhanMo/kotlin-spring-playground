package io.duhanmo.kotlinjooq.jooq.member

import io.duhanmo.jooq.tables.Members.MEMBERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(
    private val dslContext: DSLContext,
) {
    fun findAll(): List<Member> {
        return dslContext.selectFrom(MEMBERS)
            .fetch()
            .map { Member(it) }
    }

    fun save(entity: Member) {
        dslContext.insertInto(
            MEMBERS,
//            MEMBERS.ID, 신규 저장시 id null이기 때문에 명시하지 않음.
            MEMBERS.NAME,
            MEMBERS.AGE,
            MEMBERS.CREATED_AT,
            MEMBERS.UPDATED_AT,
        ).values(
//            entity.id,
            entity.name,
            entity.age,
            entity.createdAt,
            entity.updatedAt,
        ).execute()
    }

    fun findByIdOrNull(id: Int): Member? {
        return dslContext.selectFrom(MEMBERS)
            .where(MEMBERS.ID.eq(id))
            .fetchOne()
            ?.let { Member(it) }
    }

    fun update(entity: Member) {
        dslContext.update(MEMBERS)
            .set(MEMBERS.NAME, entity.name)
            .set(MEMBERS.AGE, entity.age)
            .where(MEMBERS.ID.eq(entity.id))
            .execute()
    }
}