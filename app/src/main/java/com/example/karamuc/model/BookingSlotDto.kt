package com.example.karamuc.model

import java.time.LocalDateTime

data class BookingSlotDto(
    val date: LocalDateTime?,
    val duration: Long?,
    val isAvailable: Boolean,
    val price: Int?,
)