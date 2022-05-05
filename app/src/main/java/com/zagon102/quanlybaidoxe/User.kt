package com.zagon102.quanlybaidoxe

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Int?,
    val username: String,
    var password: String,
    val role: String,
    var name: String,
    var dob: LocalDate,
    var phone: String,
    var email: String
)
