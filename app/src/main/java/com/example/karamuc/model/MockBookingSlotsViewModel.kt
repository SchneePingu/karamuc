package com.example.karamuc.model

import java.time.LocalDate
import java.time.LocalTime

class MockBookingSlotsViewModel : BookingSlotsViewModelInterface  {
    override var tabIndex: Int = 2

    override var bookingDays: List<LocalDate> = listOf(
        LocalDate.of(1999, 12,29),
        LocalDate.of(1999, 12,30),
        LocalDate.of(1999, 12,31),
        LocalDate.of(2000, 1,1),
        LocalDate.of(2000, 1,2)
    )

    override fun getBookingSlots(): List<BookingSlotsUiState> {
        return listOf(
            bookingSlotUiState,
            bookingSlotUiState,
            bookingSlotUiState,
            bookingSlotUiState,
            bookingSlotUiState
        )
    }

    override fun isLoading(): Boolean {
        return false
    }

    override fun updateTabIndex(bookingDate: LocalDate?) {}

    override fun updateTabIndex(newTabIndex: Int) {}

    override fun updateBookingDays(bookingDate: LocalDate?) {}

    override fun updateBookingSlots() {}

    private val bookingSlotUiState = BookingSlotsUiState.Success(
        slots = listOf(
            BookingSlot(
                LocalTime.of(16,30,0),
                LocalTime.of(18, 30, 0),
                30,
                "Cat Café",
                isAvailable = true,
                roomSize = 2,
                maxRoomSize = 2
            ),
            BookingSlot(
                LocalTime.of(18,30,0),
                LocalTime.of(20, 30, 0),
                40,
                "Cat Café",
                isAvailable = true,
                roomSize = 2,
                maxRoomSize = 2
            ),
        )
    )
}