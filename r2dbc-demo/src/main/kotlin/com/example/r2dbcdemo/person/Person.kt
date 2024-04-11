package com.example.r2dbcdemo.person

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("person")
data class Person(
    @Id val id: Long? = null,
    val name: String,
    val age: Int,
)
