package io.duhanmo.kotlinjooq.jooq.member

import io.duhanmo.kotlinjooq.jooq.member.dto.MemberSaveRequest
import io.duhanmo.kotlinjooq.jooq.member.dto.MemberUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MemberService(private val memberRepository: MemberRepository) {
    @Transactional(readOnly = true)
    fun findAll(): List<Member> {
        return memberRepository.findAll()
    }

    @Transactional
    fun save(request: MemberSaveRequest) {
        val now = LocalDateTime.now()
        memberRepository.save(
            Member(
                name = request.name,
                age = request.age,
                createdAt = now,
                updatedAt = now,
            )
        )
    }

    @Transactional(readOnly = true)
    fun findById(id: Int): Member {
        return memberRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Not found member id: $id")
    }

    @Transactional
    fun update(id: Int, request: MemberUpdateRequest) {

    }
}