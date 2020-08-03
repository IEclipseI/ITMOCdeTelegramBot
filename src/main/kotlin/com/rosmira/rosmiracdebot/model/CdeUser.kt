package com.rosmira.rosmiracdebot.model

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "cdeuser", uniqueConstraints = [UniqueConstraint(columnNames = ["login"], name = "login_unq")])
data class CdeUser(
        val login: String,
        val password: String,
        val antId: String,
        @Id
        val tgUserId: String) : Serializable {

    constructor() : this("", "", "", "")
}
