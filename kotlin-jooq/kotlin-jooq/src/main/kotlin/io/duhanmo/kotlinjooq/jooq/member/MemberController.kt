package io.duhanmo.kotlinjooq.jooq.member

import io.duhanmo.kotlinjooq.jooq.member.dto.MemberSaveRequest
import io.duhanmo.kotlinjooq.jooq.member.dto.MemberUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController(private val memberService: MemberService) {
    @GetMapping
    fun findAll(): ResponseEntity<List<Member>> {
        return ok(memberService.findAll())
    }

    @PostMapping
    fun save(@RequestBody request: MemberSaveRequest): ResponseEntity<Unit> {
        memberService.save(request)
        return ok().build()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Member> {
        return ok(memberService.findById(id))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody request: MemberUpdateRequest): ResponseEntity<Unit> {
        memberService.update(id, request)
        return ok().build()
    }
}