package com.zagon102.quanlybaidoxe

import java.time.LocalDateTime
import java.util.*

data class VehicleCheck(
    val brand: String,
    val seats: Int,
    val color: String,
    val plate: String,
    val checkInTime: LocalDateTime,
    val checkOutTime: LocalDateTime,
    val name: String,
    val phone: String
    ) {
    companion object {
        fun getVehicleChecks(): List<VehicleCheck> {
            return listOf(
                VehicleCheck(
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDateTime.of(2020,11,23,1,4),
                    LocalDateTime.of(2021,11,23,1,4),
                    "some name",
                    "some phone"
                ),
                VehicleCheck(
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDateTime.of(2020,11,23,1,4),
                    LocalDateTime.of(2021,11,23,1,4),
                    "some name",
                    "some phone"
                ),
                VehicleCheck(
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDateTime.of(2020,11,23,1,4),
                    LocalDateTime.of(2021,11,23,1,4),
                    "some name",
                    "some phone"
                ),
                VehicleCheck(
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDateTime.of(2020,11,23,1,4),
                    LocalDateTime.of(2021,11,23,1,4),
                    "some name",
                    "some phone"
                ),
            )
        }
    }
}
