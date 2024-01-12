package com.example.karamuc.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karamuc.mapper.BookingSlotsMapper
import com.example.karamuc.network.BookingSlotApi
import com.example.karamuc.service.BookingDaysService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val BOOKING_WEEK_INDICES = listOf<Long>(3, 4, 5, 6, 7)

class BookingSlotsViewModel : ViewModel() {
    var tabIndex: Int by mutableStateOf(0)

    var bookingDays: List<LocalDate> by mutableStateOf(listOf())

    private var wednesdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var thursdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var fridayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var saturdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var sundayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)

    private val roomIds = getRoomIds()

    fun getBookingSlots(): List<BookingSlotsUiState> {
        return listOf(
            wednesdayBookingSlots,
            thursdayBookingSlots,
            fridayBookingSlots,
            saturdayBookingSlots,
            sundayBookingSlots
        )
    }

    fun updateTabIndex(bookingDate: LocalDate?) {
        tabIndex = getTabIndex(bookingDate)
    }

    fun updateTabIndex(newTabIndex: Int) {
        tabIndex = newTabIndex
    }

    fun updateBookingDays(bookingDate: LocalDate?) {
        bookingDays = getBookingDays(bookingDate)
    }

    fun updateBookingSlots() {
        bookingDays.forEachIndexed { index, day ->
            fetchBookingSlots(day) {
                when (index) {
                    0 -> wednesdayBookingSlots = it
                    1 -> thursdayBookingSlots = it
                    2 -> fridayBookingSlots = it
                    3 -> saturdayBookingSlots = it
                    4 -> sundayBookingSlots = it
                }
            }
        }
    }

    private fun fetchBookingSlots(
        bookingDate: LocalDate,
        setUiState: (ui: BookingSlotsUiState) -> Unit
    ) {
        val bookingDateString = formatBookingDate(bookingDate)

        viewModelScope.launch {
            setUiState(BookingSlotsUiState.Loading)

            val booking = try {
                val bookingResult = BookingSlotApi
                    .retrofitService
                    .getBookingSlots(bookingDateString, roomIds)

                if (bookingResult.isEmpty()) {
                    BookingSlotsUiState.Error
                }

                BookingSlotsUiState.Success(
                    BookingDaysService.sort(
                        BookingDaysService.filterAvailable(
                            BookingSlotsMapper.map(bookingResult)
                        )
                    )
                )
            } catch (e: IOException) {
                BookingSlotsUiState.Error
            } catch (e: HttpException) {
                BookingSlotsUiState.Error
            }

            setUiState(booking)
        }
    }

    private fun formatBookingDate(bookingDate: LocalDate): String {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(bookingDate)
    }

    private fun getBookingDays(bookingDate: LocalDate?): List<LocalDate> {
        if (bookingDate == null) {
            return listOf()
        }

        val bookingDayIndex = bookingDate.dayOfWeek.value

        return BOOKING_WEEK_INDICES.map {
            bookingDate.plusDays(it - bookingDayIndex)
        }
    }

    private fun getTabIndex(bookingDay: LocalDate?): Int {
        if (bookingDay == null) {
            return 0
        }

        val tabIndex = BOOKING_WEEK_INDICES.find {
            bookingDay.dayOfWeek.value == it.toInt()
        } ?: return 0

        return tabIndex.toInt()
    }
}