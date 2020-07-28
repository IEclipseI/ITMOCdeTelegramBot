package com.rosmira.rosmiracdebot.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "cdeuser", uniqueConstraints = [UniqueConstraint(columnNames = ["login"], name = "login_unq"), UniqueConstraint(columnNames = ["tgUserId"], name = "tg_user_id_unq")])
data class CdeUser(
        val login: String,
        val password: String,
        val cdeHash: String,
        val tgUserId: String,
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1) : Serializable {

    constructor() : this("", "", "", "")
}
