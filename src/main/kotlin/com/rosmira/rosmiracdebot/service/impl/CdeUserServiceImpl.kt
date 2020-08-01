package com.rosmira.rosmiracdebot.service.impl

import com.rosmira.rosmiracdebot.model.CdeUser
import com.rosmira.rosmiracdebot.repository.CdeUserRepository
import com.rosmira.rosmiracdebot.service.CdeUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CdeUserServiceImpl: CdeUserService {
    @Autowired
    lateinit var cdeUserRepository: CdeUserRepository

    override fun getCdeUserByTgUserId(tgUserId: String) = cdeUserRepository.getByTgUserId(tgUserId)
    override fun save(cdeUser: CdeUser): CdeUser = cdeUserRepository.save(cdeUser)
}