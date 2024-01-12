package com.example.karamuc.model

sealed interface BookingSlotsUiState {
    data class Success(val slots: List<BookingSlot>) :
        BookingSlotsUiState
    object Error : BookingSlotsUiState
    object Loading : BookingSlotsUiState
}
