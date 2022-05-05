package com.zagon102.quanlybaidoxe

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

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
    var cash: String?,
    var done: String
) : Serializable {
    companion object {
        fun getVehicleChecks(): List<VehicleCheck> {
            return listOf(
                VehicleCheck(
                    null,
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDate.of(2020, 11, 23),
                    LocalDate.of(2021, 11, 23),
                    "some name",
                    "some phone", "", Constants.PENDING
                ),
                VehicleCheck(
                    null,
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDate.of(2020, 11, 23),
                    LocalDate.of(2021, 11, 23),
                    "some name",
                    "some phone", "", Constants.PENDING
                ),
                VehicleCheck(
                    null,
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDate.of(2020, 11, 23),
                    LocalDate.of(2021, 11, 23),
                    "some name",
                    "some phone", "", Constants.PENDING
                ),
                VehicleCheck(
                    null,
                    "toyota",
                    4,
                    "red",
                    "asdf",
                    LocalDate.of(2020, 11, 23),
                    LocalDate.of(2021, 11, 23),
                    "some name",
                    "some phone", "", Constants.PENDING
                ),
            )
        }
    }
}
