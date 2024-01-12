package com.example.karamuc.service

import com.example.karamuc.model.BookingSlot

class BookingDaysService {
    companion object {

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