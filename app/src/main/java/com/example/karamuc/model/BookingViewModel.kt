package com.example.karamuc.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karamuc.mapper.BookingSlotMapper
import com.example.karamuc.network.BookingSlotApi
import com.example.karamuc.service.BookingDaysService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface BookingUiState {
    data class Success(val slots: List<BookingSlot>) : BookingUiState
    object Error : BookingUiState
    object Loading : BookingUiState
}

class BookingViewModel : ViewModel() {
    var tabIndex: Int by mutableStateOf(0)

    var wednesday: BookingUiState by mutableStateOf(BookingUiState.Loading)
    var thursday: BookingUiState by mutableStateOf(BookingUiState.Loading)
    var friday: BookingUiState by mutableStateOf(BookingUiState.Loading)
    var saturday: BookingUiState by mutableStateOf(BookingUiState.Loading)
    var sunday: BookingUiState by mutableStateOf(BookingUiState.Loading)

    fun updateTabIndex(bookingDate: LocalDate?) {
        tabIndex = BookingDaysService.getTabIndex(bookingDate)
    }

    fun updateTabIndex(newTabIndex: Int) {
        tabIndex = newTabIndex
    }

    fun updateState(bookingDate: LocalDate?) {
        val days = BookingDaysService.getBookingWeek(bookingDate)

        if (days.isEmpty() || days.size != 5) {
            return
        }

        getBookingSlots(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(days[0])) {
            wednesday = it
        }
        getBookingSlots(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(days[1])) { thursday = it }
        getBookingSlots(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(days[2])) { friday = it }
        getBookingSlots(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(days[3])) { saturday = it }
        getBookingSlots(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(days[4])) { sunday = it }
    }

    private fun getBookingSlots(
        bookingDate: String,
        setter: (ui: BookingUiState) -> Unit
    ) {
        viewModelScope.launch {
            setter(BookingUiState.Loading)

            val roomIds = Room.values().map{ it.id }.joinToString(",", transform = { it })

            val booking = try {
                val bookingResult = BookingSlotApi.retrofitService.getBookingSlots(bookingDate, roomIds)
                if (bookingResult.isEmpty()) {
                    BookingUiState.Error
                }
                BookingUiState.Success(
                    BookingDaysService.sort(
                        BookingDaysService.filterAvailable(
                            BookingSlotMapper.map(bookingResult)
                        )
                    )
                )
            } catch (e: IOException) {
                BookingUiState.Error
            } catch (e: HttpException) {
                BookingUiState.Error
            }

            setter(booking)
        }
    }
}