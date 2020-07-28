package com.rosmira.rosmiracdebot.repo

import com.rosmira.rosmiracdebot.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findUserByLogin(login: String): User?
}