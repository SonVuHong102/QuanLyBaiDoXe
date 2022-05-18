package com.zagon102.quanlybaidoxe.presentation.model

data class Vehicle (
    val id: Int?,
    val username: String,
    val brand: String,
    val seats: Int,
    val color: String,
    val plate: String,
    val phone: String,
) {
    override fun toString(): String {
        return "$brand - $color - $plate"
    }
}
