package com.example.r2dbcdemo.person

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/persons")
class PersonController(private val personService: PersonService) {
    @GetMapping
    suspend fun getAllPersons(): List<Person> = personService.getAllPersons()

    @GetMapping("/{id}")
    suspend fun getPersonById(@PathVariable id: Long): Person? = personService.getPersonById(id)

    @PostMapping
    suspend fun createPerson(@RequestBody person: Person): Person = personService.createPerson(person)

    @DeleteMapping("/{id}")
    suspend fun deletePerson(@PathVariable id: Long): Unit = personService.deletePerson(id)
}