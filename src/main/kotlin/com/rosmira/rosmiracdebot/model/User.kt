package com.rosmira.rosmiracdebot.model

import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        @NaturalId
        val login: String,
        val password: String,
        val cdeHash: String,
        @NaturalId
        val tgUserId: String,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1) : Serializable {

    constructor() : this("", "", "", "")
}
