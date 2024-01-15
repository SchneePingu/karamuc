package com.example.karamuc.model

import java.time.LocalDate

interface BookingSlotsViewModelInterface {
    var tabIndex: Int

    var bookingDays: List<LocalDate>

    fun getBookingSlots(): List<BookingSlotsUiState>

    fun isLoading(): Boolean

    fun updateTabIndex(bookingDate: LocalDate?)

    fun updateTabIndex(newTabIndex: Int)

    fun updateBookingDays(bookingDate: LocalDate?)

    fun updateBookingSlots()
}