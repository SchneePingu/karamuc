package com.example.karamuc.adapter

import com.example.karamuc.model.BookingSlotDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class BookingSlotsAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: Set<Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (type == BookingSlotDto::class.java) {
            return BookingSlotsAdapter()
        }
        return null
    }
}

private class BookingSlotsAdapter: JsonAdapter<BookingSlotDto>() {

    override fun fromJson(reader: JsonReader): BookingSlotDto? {
        val jsonValue = reader.readJsonValue()
        if (jsonValue is Map<*, *>) {
                return BookingSlotDto(
                    date = readDate(jsonValue),
                    duration = readDuration(jsonValue),
                    isAvailable = readIsAvailable(jsonValue),
                    price = readPrice(jsonValue),
                )
        }

        return null
    }

    override fun toJson(writer: JsonWriter, value: BookingSlotDto?) {}

    private fun readDate(jsonValue: Map<*, *>): LocalDateTime? {
        val date = jsonValue["date"] as? String ?: return null

        return try {
            LocalDateTime.parse(date)
        } catch (_: DateTimeParseException) {
            null
        }
    }

    private fun readDuration(jsonValue: Map<*, *>): Long? {
        val value = jsonValue["duration"] as? Double ?: return null

        return value.toLong()
    }

    private fun readIsAvailable(jsonValue: Map<*,*>): Boolean {
        val value = jsonValue["available"] as? Double ?: return false

        if( value < 1.0) {
            return false
        }

        return true
    }

    private fun readPrice(jsonValue: Map<*, *>): Int? {
        val value = jsonValue["price"] as? Double ?: return null

        return try {
            value.toInt()
        } catch (_: NumberFormatException) {
            return null
        }
    }
}