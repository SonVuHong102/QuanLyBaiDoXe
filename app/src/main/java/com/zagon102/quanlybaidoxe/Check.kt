package com.zagon102.quanlybaidoxe

import java.time.LocalDate

data class Check(
    val id: Int?,
    val brand: String,
    val seats: Int,
    val color: String,
    val plate: String,
    val checkinTime: LocalDate,
    var checkoutTime: LocalDate,
    val name: String,
    val phone: String,
    var cash: Int
)
