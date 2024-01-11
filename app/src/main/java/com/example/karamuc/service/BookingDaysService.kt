package com.example.karamuc.service

import com.example.karamuc.model.BookingSlot
import java.time.LocalDate

val BOOKING_WEEK_INDICES = listOf<Long>(3, 4, 5, 6, 7)

class BookingDaysService {
    companion object {
        fun getBookingWeek(bookingDay: LocalDate?): List<LocalDate> {
            if (bookingDay == null) {
                return listOf()
            }

            val bookingDayIndex = bookingDay.dayOfWeek.value

            return BOOKING_WEEK_INDICES.map {
                bookingDay.plusDays( it - bookingDayIndex)
            }
        }

        fun getTabIndex(bookingDay: LocalDate?): Int {
            if (bookingDay == null) {
                return 0
            }

            if (bookingDay.dayOfWeek.value < 3) {
                return 0
            }

            return bookingDay.dayOfWeek.value - 3

        }

        fun filterSize(bookingSlots: List<BookingSlot>, numberOfPersons: Int?): List<BookingSlot> {
            if (numberOfPersons == null) {
                return bookingSlots
            }

            return bookingSlots.filter { isBigEnough(it, numberOfPersons)}
        }

        private fun isBigEnough(bookingSlot: BookingSlot, numberOfPersons: Int): Boolean {
            if (bookingSlot.persons == null || bookingSlot.roomSize == null) {
                return false
            }

            return bookingSlot.persons <= numberOfPersons && numberOfPersons <= bookingSlot.roomSize

        }

        fun sort(bookingSlots: List<BookingSlot>): List<BookingSlot> {
            return bookingSlots.sortedWith(
                compareBy(
                    { it.fromTime },
                    { it.toTime },
                    { it.price },
                    { it.roomName }
                )
            )
        }

        fun filterAvailable(bookingSlots: List<BookingSlot>): List<BookingSlot> {
            return bookingSlots.filter { isBookingSlotAvailable(it) }
        }

        private fun isBookingSlotAvailable(bookingSlot: BookingSlot): Boolean {
            if (!bookingSlot.isAvailable) {
                return false
            }

            return true
        }
    }
}