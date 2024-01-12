package com.example.karamuc.model

import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class BookingSlot(
    val fromTime: LocalTime?,
    val toTime: LocalTime?,
    val price: Int?,
    val roomName: String?,
    val persons: Int?,
    val roomSize: Int?,
    val isAvailable: Boolean
)

fun getPrice(bookingSlot: BookingSlot): String {
    if (bookingSlot.price == null) {
        return "k.A."
    }

    return "${bookingSlot.price} Euro"
}

fun getRoomName(bookingSlot: BookingSlot): String {
    if (bookingSlot.roomName == null) {
        return "k.A."
    }

    return bookingSlot.roomName
}

fun getTimeSlot(bookingSlot: BookingSlot): String {
    val fromTime = formatTime(bookingSlot.fromTime)
    val toTime = formatTime(bookingSlot.toTime)

    return "$fromTime - $toTime"
}

private fun formatTime(time: LocalTime?): String {
    if (time == null) {
        return "k.A."
    }

    return DateTimeFormatter.ofPattern("HH:mm").format(time)
}