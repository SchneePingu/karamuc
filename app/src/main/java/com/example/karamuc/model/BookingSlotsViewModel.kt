package com.example.karamuc.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karamuc.network.BookingSlotApi
import com.example.karamuc.service.BookingSlotService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val BOOKING_WEEK_INDICES = listOf(3, 4, 5, 6, 7)
const val BOOKING_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_TAB_INDEX = 0
const val WEDNESDAY_TAB_INDEX = 0
const val THURSDAY_TAB_INDEX = 1
const val FRIDAY_TAB_INDEX = 2
const val SATURDAY_TAB_INDEX = 3
const val SUNDAY_TAB_INDEX = 4

class BookingSlotsViewModel : ViewModel(), BookingSlotsViewModelInterface  {
    override var tabIndex: Int by mutableStateOf(DEFAULT_TAB_INDEX)

    override var bookingDays: List<LocalDate> by mutableStateOf(listOf())

    private var wednesdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var thursdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var fridayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var saturdayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)
    private var sundayBookingSlots: BookingSlotsUiState by mutableStateOf(BookingSlotsUiState.Loading)

    private val roomIds = getRoomIds()

    override fun getBookingSlots(): List<BookingSlotsUiState> {
        return listOf(
            wednesdayBookingSlots,
            thursdayBookingSlots,
            fridayBookingSlots,
            saturdayBookingSlots,
            sundayBookingSlots
        )
    }

    override fun isLoading(): Boolean {
        return getBookingSlots().any{ it is BookingSlotsUiState.Loading }
    }

    override fun updateTabIndex(bookingDate: LocalDate?) {
        tabIndex = getTabIndex(bookingDate)
    }

    override fun updateTabIndex(newTabIndex: Int) {
        tabIndex = newTabIndex
    }

    override fun updateBookingDays(bookingDate: LocalDate?) {
        bookingDays = getBookingDays(bookingDate)
    }

    override fun updateBookingSlots() {
        bookingDays.forEachIndexed { index, day ->
            fetchBookingSlots(day) {
                when (index) {
                    WEDNESDAY_TAB_INDEX -> wednesdayBookingSlots = it
                    THURSDAY_TAB_INDEX -> thursdayBookingSlots = it
                    FRIDAY_TAB_INDEX -> fridayBookingSlots = it
                    SATURDAY_TAB_INDEX -> saturdayBookingSlots = it
                    SUNDAY_TAB_INDEX -> sundayBookingSlots = it
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

                BookingSlotsUiState.Success(BookingSlotService.getBookingSlots(bookingResult))
            } catch (e: IOException) {
                BookingSlotsUiState.Error
            } catch (e: HttpException) {
                BookingSlotsUiState.Error
            }

            setUiState(booking)
        }
    }

    private fun formatBookingDate(bookingDate: LocalDate): String {
        return DateTimeFormatter.ofPattern(BOOKING_DATE_FORMAT).format(bookingDate)
    }

    private fun getBookingDays(bookingDate: LocalDate?): List<LocalDate> {
        if (bookingDate == null) {
            return listOf()
        }

        val bookingDayIndex = bookingDate.dayOfWeek.value

        return BOOKING_WEEK_INDICES.map {
            bookingDate.plusDays(it - bookingDayIndex.toLong())
        }
    }

    private fun getTabIndex(bookingDay: LocalDate?): Int {
        if (bookingDay == null) {
            return DEFAULT_TAB_INDEX
        }

        val tabIndex = BOOKING_WEEK_INDICES.indexOf(bookingDay.dayOfWeek.value)
        if (tabIndex < 0) {
            return DEFAULT_TAB_INDEX
        }

        return tabIndex
    }
}