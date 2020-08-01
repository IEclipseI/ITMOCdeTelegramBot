package com.rosmira.rosmiracdebot.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "cdeuser", uniqueConstraints = [UniqueConstraint(columnNames = ["login"], name = "login_unq")])
data class CdeUser(
        val login: String,
        val password: String,
        val cdeHash: String,
        @Id
        val tgUserId: String) : Serializable {

    constructor() : this("", "", "", "")
}
