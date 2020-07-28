package com.rosmira.rosmiracdebot.repo

import com.rosmira.rosmiracdebot.model.CdeUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CdeUserRepository: CrudRepository<CdeUser, Long> {
    fun findCdeUserByLogin(login: String): CdeUser?
}