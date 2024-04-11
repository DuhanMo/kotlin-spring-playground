package com.example.r2dbcdemo.person

import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class PersonService(private val personRepository: PersonRepository) {
    suspend fun getAllPersons(): List<Person> = personRepository.findAll().toList()

    suspend fun getPersonById(id: Long): Person? = personRepository.findById(id)

    suspend fun createPerson(person: Person): Person = personRepository.save(person)

    suspend fun deletePerson(id: Long) = personRepository.deleteById(id)
}