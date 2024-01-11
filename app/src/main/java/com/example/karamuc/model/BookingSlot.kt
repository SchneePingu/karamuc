package com.example.karamuc.model

import java.time.LocalTime


data class BookingSlot(
    val fromTime: LocalTime?,
    val toTime: LocalTime?,
    val price: Int?,
    val roomName: String?,
    val persons: Int?,
    val roomSize: Int?,
    val isAvailable: Boolean
)