package com.rosmira.rosmiracdebot.service

import com.rosmira.rosmiracdebot.model.CdeUser

interface CdeUserService {
    fun getCdeUserByTgUserId(tgUserId: String): CdeUser?
    fun save(cdeUser: CdeUser): CdeUser
}