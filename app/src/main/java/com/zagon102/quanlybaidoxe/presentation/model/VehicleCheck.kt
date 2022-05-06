package com.zagon102.quanlybaidoxe.presentation.model

import com.zagon102.quanlybaidoxe.ultis.Constants
import java.io.Serializable
import java.time.LocalDate

data class VehicleCheck(
    val id: Int?,
    val brand: String,
    val seats: Int,
    val color: String,
    val plate: String,
    val checkInDate: LocalDate,
    var checkOutDate: LocalDate?,
    val name: String,
    val phone: String,
    var cash: Long?,
    var done: String
) : Serializable
