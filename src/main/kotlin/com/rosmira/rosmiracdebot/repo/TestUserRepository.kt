package com.rosmira.rosmiracdebot.repo

import com.rosmira.rosmiracdebot.model.TestUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TestUserRepository: CrudRepository<TestUser, Long> {
    fun findByPas(pas: String): Iterable<TestUser>
}