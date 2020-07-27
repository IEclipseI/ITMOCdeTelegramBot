package com.rosmira.rosmiracdebot.bot

import com.rosmira.rosmiracdebot.model.TestUser
import com.rosmira.rosmiracdebot.repo.TestUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Qwe {

    @Autowired
    lateinit var repository: TestUserRepository

    @RequestMapping("/save")
    fun save(): String {
        repository.save(TestUser("Jack", "Smith"))
        repository.save(TestUser("Adam", "Johnson"))
        repository.save(TestUser("Kim", "Smith"))
        repository.save(TestUser("David", "Williams"))
        repository.save(TestUser("Peter", "Davis"))

        return "Done"
    }

    @RequestMapping("/findall")
    fun findAll() = repository.findAll()

    @RequestMapping("/findbyid/{id}")
    fun findById(@PathVariable id: Long) = repository.findById(id)

    @RequestMapping("findbylastname/{lastName}")
    fun findByLastName(@PathVariable lastName: String) = repository.findByPas(lastName)

}