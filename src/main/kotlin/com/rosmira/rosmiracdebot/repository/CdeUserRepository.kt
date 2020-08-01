package com.rosmira.rosmiracdebot.repository

import com.rosmira.rosmiracdebot.model.CdeUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CdeUserRepository : JpaRepository<CdeUser, Long> {
    fun findCdeUserByLogin(login: String): CdeUser?
    fun existsCdeUserByLogin(login: String): Boolean
    fun getByTgUserId(tgUserId: String): CdeUser?
}