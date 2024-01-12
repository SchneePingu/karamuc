package com.example.karamuc.service

import com.example.karamuc.mapper.BookingSlotsMapper
import com.example.karamuc.model.BookingSlot
import com.example.karamuc.model.BookingSlotDto

class BookingSlotService {
    companion object {
        fun getBookingSlots(dto: Map<String, List<BookingSlotDto>>): List<BookingSlot> {
            return sort(filterByAvailability(BookingSlotsMapper.map(dto))
            )
        }

        private fun sort(bookingSlots: List<BookingSlot>): List<BookingSlot> {
            return bookingSlots.sortedWith(
                compareBy(
                    { it.fromTime },
                    { it.toTime },
                    { it.price },
                    { it.roomName }
                )
            )
        }

        fun filterByNumberOfPersons(bookingSlots: List<BookingSlot>, numberOfPersons: Int?): List<BookingSlot> {
            if (numberOfPersons == null) {
                return bookingSlots
            }

            return bookingSlots.filter { hasSufficientRoomSize(it, numberOfPersons)}
        }

        private fun hasSufficientRoomSize(bookingSlot: BookingSlot, numberOfPersons: Int): Boolean {
            if (bookingSlot.roomSize == null || bookingSlot.maxRoomSize == null) {
                return false
            }

            return bookingSlot.roomSize <= numberOfPersons && numberOfPersons <= bookingSlot.maxRoomSize

        }

        private fun filterByAvailability(bookingSlots: List<BookingSlot>): List<BookingSlot> {
            return bookingSlots.filter { it.isAvailable }
        }
    }
}