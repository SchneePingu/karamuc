package com.example.karamuc.network

import com.example.karamuc.adapter.BookingSlotsAdapterFactory
import com.example.karamuc.model.BookingSlotDto
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://www.yokochokaraoke.de"

val moshi: Moshi = Moshi.Builder()
    .add(BookingSlotsAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookingSlotApiService {
    @GET("wp-json/yokop/v1/available-booking-slots")
    suspend fun getBookingSlots(
        @Query("booking_date") bookingDate: String,
        @Query("product_ids") roomIds: String,
    ): Map<String, List<BookingSlotDto>>
}

object BookingSlotApi {
    val retrofitService: BookingSlotApiService by lazy {
        retrofit.create(BookingSlotApiService::class.java)
    }
}