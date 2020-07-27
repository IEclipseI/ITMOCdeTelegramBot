package com.rosmira.rosmiracdebot.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "testuser")
@IdClass(TestUserId::class)
data class TestUser(
        @Id
        var firstname: String,
        var pas: String,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1) : Serializable {
    constructor(): this("", "")
}

data class TestUserId(var firstname: String, var id: Long) : Serializable {
    constructor() : this("", -1)
}