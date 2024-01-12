package com.example.karamuc.mapper

import com.example.karamuc.model.BookingSlot
import com.example.karamuc.model.BookingSlotDto
import com.example.karamuc.model.getRoomById

class BookingSlotsMapper {
    companion object {
        fun map(source: Map<String, List<BookingSlotDto>>): List<BookingSlot> {
            return source.flatMap { (roomId, list) ->
                val room = getRoomById(roomId)
                val roomName = room?.displayName
                val persons = room?.persons
                val size = room?.size

                list.map { dto ->
                    val fromTime = dto.date?.toLocalTime()
                    val timeSpan: Long = if (dto.duration == null) 0 else dto.duration * 60
                    val toTime = fromTime?.plusMinutes(timeSpan)

                    BookingSlot(
                        fromTime = fromTime,
                        toTime = toTime,
                        roomName = roomName,
                        price = dto.price,
                        isAvailable = dto.isAvailable,
                        persons = persons,
                        roomSize = size
                    )
                }
            }
        }
    }
}