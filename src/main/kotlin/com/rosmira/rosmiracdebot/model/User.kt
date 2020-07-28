package com.rosmira.rosmiracdebot.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "user")
@IdClass(UserId::class)
data class User(
        @Id
        val login: String,
        val password: String,
        val cdeHash: String,
        @Id
        val tgUserId: String,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1) : Serializable {

    constructor() : this("", "", "", "")
}

data class UserId(val login: String, val tgUserId: String, val id: Long) : Serializable {
    constructor() : this("", "", -1)
}